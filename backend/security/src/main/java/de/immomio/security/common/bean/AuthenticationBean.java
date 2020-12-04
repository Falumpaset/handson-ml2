package de.immomio.security.common.bean;

/**
 * @author Johannes Hiemer.
 */
public class AuthenticationBean {

    private String name;

    private String token;

    private Long id;

    public AuthenticationBean(String name, String token, Long id) {
        super();
        this.name = name;
        this.token = token;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
