package de.immomio.controller.filer.landlord.file.contract;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.controller.AbstractFilerController;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.landlord.service.contract.DigitalContractSignerService;
import de.immomio.security.common.bean.DigitalContractSignToken;
import de.immomio.security.service.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Controller
@RequestMapping(value = "/dmv")
public class ContractDownloadController extends AbstractFilerController<LandlordS3FileManager> {

    private final DigitalContractSignerService digitalContractSignerService;
    private final JWTTokenService jwtTokenService;
    private final LandlordS3FileManager landlordS3FileManager;

    public ContractDownloadController(
            DigitalContractSignerService digitalContractSignerService,
            JWTTokenService jwtTokenService,
            LandlordS3FileManager landlordS3FileManager
    ) {
        this.digitalContractSignerService = digitalContractSignerService;
        this.jwtTokenService = jwtTokenService;
        this.landlordS3FileManager = landlordS3FileManager;
    }

    @GetMapping("/download")
    public ResponseEntity downloadContract(@RequestParam("token") String token, @RequestParam("identifier") String identifier, HttpServletRequest request) throws IOException {
        DigitalContractSignToken contractSignToken = jwtTokenService.validateContractSignToken(token);
        S3File signedContract = digitalContractSignerService.getSignedContract(contractSignToken.getSignerId(), identifier);

        return download(landlordS3FileManager, signedContract.getUrl(), signedContract.getName(), signedContract.isEncrypted(), request);
    }
}
