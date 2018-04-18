package space.ballistix.metamaskauth.module.web.domain.dto;

public class LoginRequestDto {
    private String signature;
    private String address;

    protected LoginRequestDto(){}

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
