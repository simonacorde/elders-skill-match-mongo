package diploma.elders.up.dao.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class User {
    private Integer id;
    private Senior seniors;
    private Company companies;
    private String token;
    private String type;
    private String forename;
    private String surname;
    private String email;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private String telephone;
    private String mobile;
    private String contactMethod;
    private String picture;
    private String skype;
    private String facetime;

    public User() {
    }

    public User(String token, String type, String forename, String surname, String email) {

        this.token = token;
        this.type = type;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

    public User(Senior seniors, Company companies,  String token, String type,
                String forename, String surname, String email) {
        this.seniors = seniors;
        this.companies = companies;

        this.token = token;
        this.type = type;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    public Senior getSeniors() {
        return this.seniors;
    }

    public void setSeniors(Senior seniors) {
        this.seniors = seniors;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public Company getCompanies() {
        return this.companies;
    }

    public void setCompanies(Company companies) {
        this.companies = companies;
    }


    @Column(name = "token", unique = true, nullable = false, columnDefinition = "char(64)")
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "type", nullable = false, columnDefinition = "enum('sme','senior')")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "forename", nullable = false, length = 100)
    public String getForename() {
        return this.forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    @Column(name = "surname", nullable = false, length = 100)
    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name = "email", nullable = false, length = 200)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address", nullable = false, length = 200)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "postcode", nullable = false, length = 50)
    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "city", nullable = false, length = 100)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "country", nullable = false, columnDefinition="char(2)")
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "telephone", nullable = false, length = 50)
    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(name = "mobile", nullable = false, length = 50)
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "contact_method", nullable = false, length = 100)
    public String getContactMethod() {
        return this.contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    @Column(name = "picture", nullable = false, length = 50)
    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column(name = "skype", nullable = false, length = 50)
    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    @Column(name = "facetime", nullable = false, length = 50)
    public String getFacetime() {
        return facetime;
    }

    public void setFacetime(String facetime) {
        this.facetime = facetime;
    }

}
