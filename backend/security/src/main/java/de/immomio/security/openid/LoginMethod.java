package de.immomio.security.openid;

public enum LoginMethod {

    DEFAULT("immomio"),
    FACEBOOK("facebook"),
    GOOGLE("google");

    private String kcIdpHint;

    LoginMethod(String kcIdpHint) {
        this.kcIdpHint = kcIdpHint;
    }

    public String getKcIdpHint() {
        return kcIdpHint;
    }
}
