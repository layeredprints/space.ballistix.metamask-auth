package space.ballistix.metamaskauth.module.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import space.ballistix.metamaskauth.container.configuration.JWTConfig;
import space.ballistix.metamaskauth.module.web.domain.dto.LoginRequestDto;
import space.ballistix.metamaskauth.module.web.util.SecurityUtil;
import space.ballistix.metamaskauth.module.web.util.SignatureUtil;

import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

@Controller
@RequestMapping("/")
public class WebController {
    private final JWTConfig jwtConfig;

    @Autowired
    public WebController(JWTConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }


    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/login", method= RequestMethod.POST)
    public ResponseEntity doLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) throws SignatureException {
        String message = "metamask-auth demo";
        String signerAddress = SignatureUtil.getAddressFromSignature(loginRequestDto.getSignature(), message);

        if(signerAddress.equalsIgnoreCase(loginRequestDto.getAddress())){
            String token = SecurityUtil.generateJWT(signerAddress, jwtConfig.getSecret());
            response.setHeader("Set-Cookie", "Authorization=" + token);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(value = {"/welcome", "/"})
    public String welcome(Model model){
        if(SecurityUtil.isSignedIn()){
            model.addAttribute("address", SecurityUtil.getPrincipal());
            return "welcome";
        }
        else {
            return "redirect:login";
        }
    }
}