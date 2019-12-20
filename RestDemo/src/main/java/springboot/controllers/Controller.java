package springboot.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Controller {
    //    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private FundRecordRepository fundRecordRepository;
//
//    @Autowired
//    private TransferRecordRepository transferRecordRepository;
//
    @ApiOperation(value = "Get all person")
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public String index() {
        return "Greetings from all Spring Boots!";
    }

//    @ApiOperation(value = "Change password for a user")
//    @RequestMapping(path = "/changepass", method = RequestMethod.POST)
//    public ResponseEntity<?> changePassword(
//            @RequestHeader(value = "Authorization") String authToken,
//            @RequestParam long userId,
//            @RequestParam String oldPassword,
//            @RequestParam String newPassword) {
//
//        logger.info("Change password request for userID: " + userId);
//
//        ResponseBuilder responseBuilder = new ResponseBuilder();
//
//        if(!UserValidator.validateUserId(userId)) {
//            throw new BadCredentialsException("Invalid user");
//        }
//
//        EtaleUser etaleUser = userRepository.getOne(userId);
//
//        if(crypt.matches(oldPassword, etaleUser.getPassword())) {
//            etaleUser.setPassword(crypt.encode(newPassword));
//            userRepository.saveAndFlush(etaleUser);
//        }
//        else {
//            responseBuilder.addEntry("Password", "Invalid password");
//            logger.info("Change password failed for userID: " + userId);
//
//            return new ResponseEntity<Map<String, String>>(responseBuilder.getResponse(), HttpStatus.BAD_REQUEST);
//        }
//
//        etaleUser.setPassword(crypt.encode(newPassword));
//        responseBuilder.addEntry("Password", "Updated");
//        return new ResponseEntity<Map<String, String>>(responseBuilder.getResponse(), HttpStatus.OK);
//    }
//
//    @Modifying
//    @Transactional
//    @ApiOperation(value = "Create a user")
//    @RequestMapping(path = "/create", method = RequestMethod.POST)
//    public ResponseEntity<?> addUser(
//            @RequestParam String email,
//            @RequestParam String firstName,
//            @RequestParam String lastName,
//            @RequestParam String jobTitle,
//            @RequestParam String company,
//            @RequestParam String phone,
//            @RequestParam String address,
//            @RequestParam String password) {
//
//        ResponseBuilder responseBuilder = new ResponseBuilder();
//
//        responseBuilder.checkForEmpty("Email", email);
//        responseBuilder.checkForEmpty("First Name", firstName);
//        responseBuilder.checkForEmpty("Last Name", lastName);
//        responseBuilder.checkForEmpty("Title", jobTitle);
//        responseBuilder.checkForEmpty("Phone", phone);
//        responseBuilder.checkForEmpty("Company", company);
//
//        String secretKey = UserValidator.getRandomSecretKey(20);
//
//        logger.info("New user: " + email + " : " + firstName + " : " + lastName + " : " + jobTitle + " : " + company + " : " + phone + " : " +  address);
//
//        if(responseBuilder.hasResponse()) {
//            return new ResponseEntity<Map<String, String>>(responseBuilder.getResponse(), HttpStatus.BAD_REQUEST);
//        }
//
//        if(userRepository.findByEmail(email) != null) {
//            responseBuilder.addEntry("Email", "Account already exists");
//            logger.info("Attempt to create a user with existing email: " + email);
//            return new ResponseEntity<Map<String, String>>(responseBuilder.getResponse(), HttpStatus.BAD_REQUEST);
//        }
//
//        // TODO: Should we assign to default org instead?
//        OrganizationData organization = organizationDataRepository.saveAndFlush(new OrganizationData(null, email));
//
//        orgExchangeConfigDataRepository.saveAndFlush(new OrganizationConfigData(null, organization.getId(), getDefaultOrg(email), null, null));
//
//        EtaleUser etaleUser = userRepository.saveAndFlush(new EtaleUser(null, email, firstName, lastName, jobTitle, company, phone,
//                address, crypt.encode(password), false, secretKey, organization.getId(), "ADMIN")); // TODO check if secret key and backup code needs to be encrypted
//
//        etaleUser.setOrgNodeMappings(ROOT_BOOKNAME);
//        return new ResponseEntity<EtaleUser>(etaleUser, HttpStatus.OK);
//    }
//
//    @ApiOperation(value = "Get all users")
//    @RequestMapping(value = "/getall", method = RequestMethod.GET)
//    public List<EtaleUser> getallUser(@RequestHeader(value = "Authorization") String authToken) {
//        // TODO: only allow tokens from admin users here
//        return userRepository.findAll();
//    }
}