package de.immomio.importer.server;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import de.immomio.importer.server.data.FtpAccessCustomerResolver;
import de.immomio.importer.server.ftplet.ImportServerUser;
import de.immomio.importer.server.ftplet.SimpleUserManager;
import de.immomio.importer.server.handler.FileDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.util.security.bouncycastle.BouncyCastleGeneratorHostKeyProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.UnknownCommandFactory;
import org.apache.sshd.server.subsystem.sftp.Handle;
import org.apache.sshd.server.subsystem.sftp.SftpEventListener;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.google.common.io.Files.createTempDir;
import static de.immomio.importer.server.ftplet.UserType.OPENIMMO;
import static de.immomio.importer.server.ftplet.UserType.RELION;


/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister, Maik Kingma
 */

@Slf4j
@Service
public class CustomServer {

    private static final String RELION_PATH = "/relion";

    private static final String OPENIMMO_PATH = "/openimmo";

    private final FtpAccessCustomerResolver ftpAccessCustomerResolver;

    private final FileDispatcher fileDispatcher;

    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.ftp-path}")
    private String ftpPath;

    @Value("${ftp.server.port}")
    private int port;

    @Value("${ftp.server.passive_mode.host}")
    private String host;

    @Value("${ftp.server.passive_mode.start_port}")
    private int startPort;

    @Value("${ftp.server.passive_mode.end_port}")
    private int endPort;

    @Value("${ftp.server.sftpport}")
    private int sftpPort;

    @Value("${ftp.server.concurrentlogins}")
    private int concurrentLogins;

    @Value("${ftp.server.loginsperip}")
    private int loginsPerIp;

    @Value("${ftp.server.keypath}")
    private String keyPath;

    private UserManager userManager;

    private File rootFolder;

    @Autowired
    public CustomServer(FtpAccessCustomerResolver ftpAccessCustomerResolver, FileDispatcher fileDispatcher,
                        RestTemplate restTemplate) {
        this.ftpAccessCustomerResolver = ftpAccessCustomerResolver;
        this.fileDispatcher = fileDispatcher;
        this.restTemplate = restTemplate;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    private Collection<FtpAccess> getAllUsersPaginated(Collection<FtpAccess> ftpAccesses, String url) {

        ResponseEntity<PagedModel<FtpAccess>> ftpAccessesEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<PagedModel<FtpAccess>>() {
                });

        PagedModel<FtpAccess> body = ftpAccessesEntity.getBody();
        if (body != null) {
            ftpAccesses.addAll(body.getContent());
            Optional<Link> nextLink = body.getNextLink();
            if (nextLink.isPresent()) {
                return getAllUsersPaginated(ftpAccesses, nextLink.get().getHref());
            }
        }

        return ftpAccesses;
    }

    @PostConstruct
    public void setupUser() throws FtpException {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        log.info("Start reloading FTP-Users.");

        File folder = createTempDir();

        rootFolder = folder;

        Collection<FtpAccess> ftpAccesses = getAllUsersPaginated(new HashSet<>(), apiUrl + ftpPath + "?size=200");

        if (this.userManager == null) {
            this.userManager = new SimpleUserManager();
        }

        HashSet<String> userNames = new HashSet<>(Arrays.asList(userManager.getAllUserNames()));

        Md5PasswordEncryptor pe = new Md5PasswordEncryptor();
        log.info("ACCESSES SIZE: " + ftpAccesses.size());
        for (FtpAccess ftpAccess : ftpAccesses) {
            String userName = ftpAccess.getUsername() + "";
            log.info("start loading default " + userName);
            String openimmoUser = userName + "-openimmo";
            String relionUser = userName + "-relion";

            ImportServerUser userDefault = (ImportServerUser) userManager.getUserByName(openimmoUser);
            ImportServerUser userRelion = (ImportServerUser) userManager.getUserByName(relionUser);

            if (userDefault == null || userRelion == null) {

                String basePath = folder.getAbsolutePath() + File.separator + ftpAccess.getId();
                File customDirectory = new File(basePath);
                customDirectory.mkdir();
                customDirectory.setWritable(true);
                customDirectory.setReadable(true);
                customDirectory.setExecutable(true);

                ConcurrentLoginPermission concurrentLoginPermission = new ConcurrentLoginPermission(concurrentLogins,
                        loginsPerIp);
                WritePermission writePermission = new WritePermission();
                List<Authority> authorities = new ArrayList<>();
                authorities.add(writePermission);
                authorities.add(concurrentLoginPermission);


                if (userDefault == null) {
                    log.info("loading default " + userName);
                    new File(basePath + OPENIMMO_PATH).mkdirs();

                    userDefault = new ImportServerUser(OPENIMMO,
                            userName,
                            ftpAccess.isEnabled(),
                            authorities,
                            customDirectory.getAbsolutePath() + OPENIMMO_PATH + File.separator);
                }

                if (userRelion == null) {
                    log.info("loading relion " + userName);
                    new File(basePath + RELION_PATH).mkdirs();
                    userRelion = new ImportServerUser(
                            RELION,
                            userName,
                            ftpAccess.isEnabled(),
                            authorities,
                            customDirectory.getAbsolutePath() + RELION_PATH + File.separator);
                }

            } else {
                userNames.remove(openimmoUser);
                userNames.remove(relionUser);

                log.info("Update customer with id: " + userName);
            }

            userDefault.setPassword(pe.encrypt(ftpAccess.getUserPassword()));
            userRelion.setPassword(pe.encrypt(ftpAccess.getUserPassword()));

            this.userManager.save(userDefault);
            this.userManager.save(userRelion);
        }

        //What is the purpose of this loop?
        for (String userName : userNames) {
            log.info("Remove customer with id: " + userName);

            userManager.delete(userName);
        }

        log.info("Finished reloading FTP-Users.");
    }

    public void startSftpServer() throws URISyntaxException, IOException {
        log.info("Starting SftpServer ...");

        File error = new File(rootFolder, "error");
        error.mkdirs();

        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(error.toPath()));
        sshServer.setPort(sftpPort);

        log.info("Port configuration set to: " + sshServer.getPort());
        log.info("KEYPATH: " + keyPath);

        KeyPairProvider keyPairProvider = new BouncyCastleGeneratorHostKeyProvider(Path.of(keyPath.trim()));

        sshServer.setCommandFactory(UnknownCommandFactory.INSTANCE);

        sshServer.setPasswordAuthenticator((username, password, session) -> {
            User user;
            try {
                user = userManager.authenticate(new UsernamePasswordAuthentication(username, password));
            } catch (AuthenticationFailedException e) {
                log.error(e.getMessage(),e);
                user = null;
            }

            if (user == null) {
                return false;
            }

            Path folder = new File(user.getHomeDirectory()).toPath();

            sshServer.setFileSystemFactory(new VirtualFileSystemFactory(folder));

            return true;
        });

        SftpSubsystemFactory sf = new SftpSubsystemFactory();
        sf.addSftpEventListener(new CustomSftpEventListener(fileDispatcher));

        sshServer.setSubsystemFactories(Collections.singletonList(sf));

        try {
            sshServer.setKeyPairProvider(keyPairProvider);
            sshServer.start();
            log.info("SftpServer started ...");
        } catch (IOException e) {
            log.error("Error startig Server ...", e);
        }
    }

    public void startServer() throws FtpException {
        log.info("Starting FtpServer ...");

        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(port);
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory
                = new DataConnectionConfigurationFactory();
        dataConnectionConfigurationFactory.setPassivePorts(startPort + "-" + endPort);
        dataConnectionConfigurationFactory.setPassiveExternalAddress(host);

        log.info("PassiveExternalAddress -> " + dataConnectionConfigurationFactory.getPassiveExternalAddress());

        DataConnectionConfiguration dataConnectionConfiguration = dataConnectionConfigurationFactory
                                                                          .createDataConnectionConfiguration();
        listenerFactory.setDataConnectionConfiguration(dataConnectionConfiguration);

        log.info("Port configuration set to: " + listenerFactory.getPort());

        FtpServerFactory factory = new FtpServerFactory();
        factory.setUserManager(this.userManager);

        Map<String, Ftplet> ftpLets = new HashMap<>();
        ftpLets.put("default", new CustomFtplet(fileDispatcher));

        factory.setFtplets(ftpLets);
        factory.addListener("default", listenerFactory.createListener());

        FtpServer server = factory.createServer();

        server.start();
        log.info("FtpServer started ...");
    }

    /**
     * @author Bastian Bliemeister, Maik Kingma
     */
    public class CustomFtplet extends DefaultFtplet {

        private FileDispatcher fileDispatcher;

        CustomFtplet(FileDispatcher fileDispatcher) {
            this.fileDispatcher = fileDispatcher;
        }

        @Override
        public FtpletResult onLogin(FtpSession ftpSession, FtpRequest ftpRequest) {
            ImportServerUser user = (ImportServerUser) ftpSession.getUser();
            ftpAccessCustomerResolver.loadCustomer("Login successful for User: ", user);

            return null;
        }

        @Override
        public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) {
            try {
                ImportServerUser user = (ImportServerUser) session.getUser();
                EntityModel<LandlordCustomer> landlordCustomerResource =
                        ftpAccessCustomerResolver.loadCustomer("Upload Request for User: ", user);
                Objects.requireNonNull(user).setCustomer(landlordCustomerResource);
                fileDispatcher.scanFolder(user.getHomeDirectory(), user.getCustomer(), user.getType());
            } catch (Exception e) {
                log.warn("Error retrieving User ...", e);
            }

            return null;
        }

        @Override
        public FtpletResult onDisconnect(FtpSession ftpSession) {
            ImportServerUser user = (ImportServerUser) ftpSession.getUser();
            ftpAccessCustomerResolver.loadCustomer("Disconnected User: ", user);

            return null;
        }
    }

    /**
     * @author Bastian Bliemeister.
     */
    public class CustomSftpEventListener implements SftpEventListener {

        private FileDispatcher fileDispatcher;

        CustomSftpEventListener(FileDispatcher fileDispatcher) {
            this.fileDispatcher = fileDispatcher;
        }

        @Override
        public void closed(ServerSession session, String remoteHandle, Handle localHandle, Throwable thrown) {
            ImportServerUser user;
            try {
                user = getUser(session.getUsername());
                EntityModel<LandlordCustomer> landlordCustomerResource =
                        ftpAccessCustomerResolver.loadCustomer("Upload Request for User: ", user);
                Objects.requireNonNull(user).setCustomer(landlordCustomerResource);
                fileDispatcher.scanFolder(user.getHomeDirectory(), user.getCustomer(), user.getType());
            } catch (Exception e) {
                log.warn("Error sending File ...", e);
            }
        }

        @Override
        public void destroying(ServerSession session) {
            ImportServerUser user;
            try {
                user = getUser(session);
            } catch (FtpException e) {
                log.warn("Error retrieving User ...", e);
                return;
            }
            ftpAccessCustomerResolver.loadCustomer("Disconnected User: ", user);
        }

        @Override
        public void initialized(ServerSession session, int version) {
            ImportServerUser user;
            try {
                user = getUser(session);
            } catch (FtpException e) {
                log.warn("Error retrieving User ...", e);
                return;
            }

            ftpAccessCustomerResolver.loadCustomer("Login successful for User: ", user);
        }

        private ImportServerUser getUser(ServerSession session) throws FtpException {
            return getUser(session.getUsername());
        }

        private ImportServerUser getUser(String username) throws FtpException {
            if (userManager == null) {
                return null;
            }

            return (ImportServerUser) userManager.getUserByName(username);
        }
    }
}
