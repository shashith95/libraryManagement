package listMembers;

public class MemberDto {
    private Long id;
    private String name;
    private String code;
    private String mobile;
    private String email;
    private Boolean status;

    public MemberDto(String name, String code, String mobile, String email, Boolean status) {
        this.name = name;
        this.code = code;
        this.mobile = mobile;
        this.email = email;
        this.status = status;
    }

    public MemberDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
