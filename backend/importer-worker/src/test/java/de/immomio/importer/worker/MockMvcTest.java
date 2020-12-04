/**
 *
 */
package de.immomio.importer.worker;

/**
 * import static
 * org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
 * import static
 * org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
 * import static
 * org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
 *
 * import java.util.HashSet; import java.util.List; import java.util.Set;
 *
 * import org.junit.Before; import org.junit.Rule; import
 * org.junit.runner.RunWith; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.ConfigFileApplicationContextInitializer; import
 * org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
 * import
 * org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
 * import org.springframework.security.core.userdetails.UserDetails; import
 * org.springframework.test.context.ActiveProfiles; import
 * org.springframework.test.context.ContextConfiguration; import
 * org.springframework.test.context.junit4.SpringJUnit4ClassRunner; import
 * org.springframework.test.context.web.WebAppConfiguration; import
 * org.springframework.test.web.servlet.MockMvc; import
 * org.springframework.test.web.servlet.request.RequestPostProcessor; import
 * org.springframework.test.web.servlet.setup.MockMvcBuilders; import
 * org.springframework.transaction.annotation.Transactional; import
 * org.springframework.web.context.WebApplicationContext;
 *
 * import com.fasterxml.jackson.databind.ObjectMapper;
 *
 * import de.immomio.Application; import de.immomio.model.customer.Customer;
 * import de.immomio.model.repositories.user.GroupRepository; import
 * de.immomio.model.repositories.user.UserRepository; import
 * de.immomio.model.user.Group; import de.immomio.model.user.User; import
 * edu.emory.mathcs.backport.java.util.Arrays;
 *
 * /**
 *
 * @author Johannes Hiemer.
 *
 */
/**
 * @Transactional
 * @SpringBootTest
 * @WebAppConfiguration
 * @ContextConfiguration(classes = { Application.class }, initializers =
 *                               ConfigFileApplicationContextInitializer.class )
 * @RunWith(SpringJUnit4ClassRunner.class) @ActiveProfiles(profiles = {
 *                                         "development" }) public abstract
 *                                         class MockMvcTest {
 *
 *                                         protected static String
 *                                         DEFAULT_CUSTOMER =
 *                                         "customer@msh.host";
 *
 *                                         protected static String
 *                                         DEFAULT_CUSTOMER_EMPLOYEE =
 *                                         "employee@msh.host";
 *
 *                                         protected static String DEFAULT_ADMIN
 *                                         = "admin@msh.host";
 *
 *                                         protected static long
 *                                         CUSTOMER_GROUP_ID = 1260;
 *
 *                                         protected static long
 *                                         CUSTOMER_EMPLOYEE_GROUP_ID = 1290;
 *
 *                                         protected static long ADMIN_GROUP_ID
 *                                         = 1291;
 *
 *                                         protected static String SEPARATOR =
 *                                         "/";
 *
 *                                         protected static final String
 *                                         DELETE_EXAMPLE = "-delete-example";
 *
 *                                         protected static final String
 *                                         UPDATE_EXAMPLE = "-update-example";
 *
 *                                         protected static final String
 *                                         CREATE_EXAMPLE = "-create-example";
 *
 *                                         protected static final String
 *                                         ONE_EXAMPLE = "-one-example";
 *
 *                                         protected static final String
 *                                         LIST_EXAMPLE = "-list-example";
 *
 *                                         private GrantedAuthoritiesMapper
 *                                         authoritiesMapper = new
 *                                         NullAuthoritiesMapper();
 *
 *                                         private ObjectMapper objectMapper =
 *                                         new ObjectMapper();
 *
 *                                         protected MockMvc mockMvc;
 *
 *                                         protected static final String
 *                                         AUTHORIZATION_HEADER =
 *                                         "Authorization";
 *
 * @Autowired private WebApplicationContext webApplicationContext;
 *
 * @Autowired private UserRepository userRepository;
 *
 * @Autowired private GroupRepository groupRepository;
 *
 * @Rule public JUnitRestDocumentation restDocumentation = new
 *       JUnitRestDocumentation("target/generated-snippets");
 *
 * @Before public final void initMockMvc() throws Exception { mockMvc =
 *         MockMvcBuilders .webAppContextSetup(webApplicationContext)
 *         .apply(documentationConfiguration(this.restDocumentation))
 *         .apply(springSecurity()) .build(); }
 *
 *         @SuppressWarnings("unchecked") protected List<Group> groups(Long...
 *         ids) { return groupRepository.findAll(Arrays.asList(ids)); }
 *
 *         protected void user(String email, Customer customer, List<Group>
 *         groups) { Set<Group> groupSet = new HashSet<Group>(groups); User user
 *         = UserData.user(email, groupSet, customer);
 *
 *         userRepository.save(user); }
 *
 *         protected RequestPostProcessor impersonate(String name) { return
 *         this.requester(name); }
 *
 *         private RequestPostProcessor requester(String name) { UserDetails
 *         userDetails = userRepository.findByEmail(name);
 *
 *         Authentication authentication = new
 *         OpenIDRelyingPartyToken(userDetails,
 *         authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
 *
 *         return authentication(authentication); }
 *
 *
 *         }
 **/