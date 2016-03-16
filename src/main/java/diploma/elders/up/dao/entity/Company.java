package diploma.elders.up.dao.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Simonas on 2/17/2016.
 */
@Entity
public class Company {
    private Integer id;
    private boolean setupComplete;
    private String name;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private String contactName;
    private String telephone;
    private String mobile;
    private String industryArea;
    private String profile;
    private Set<Opportunity> opportunitieses = new HashSet<Opportunity>(0);
    private List<User> userses = new ArrayList<User>(0);
    private Set<Endorsement> endorsementses = new HashSet<Endorsement>(0);

    public Company() {
    }

    public Company(boolean setupComplete, String name, String address, String postcode,
                   String city, String country, String contactName, String telephone, String mobile, String email,
                   String industryArea, String profile) {
        this.setupComplete = setupComplete;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.contactName = contactName;
        this.telephone = telephone;
        this.mobile = mobile;
        this.industryArea = industryArea;
        this.profile = profile;
    }

    public Company( boolean setupComplete, String name, String address, String postcode,
                    String city, String country, String contactName, String telephone, String mobile, String email,
                    String industryArea, String profile, Set<Opportunity> opportunitieses, List<User> userses, Set<Endorsement> endorsementses) {

        this.setupComplete = setupComplete;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.contactName = contactName;
        this.telephone = telephone;
        this.mobile = mobile;
        this.industryArea = industryArea;
        this.profile = profile;
        this.opportunitieses = opportunitieses;
        this.userses = userses;
        this.endorsementses = endorsementses;
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


    @Column(name = "setup_complete", nullable = false)
    public boolean isSetupComplete() {
        return this.setupComplete;
    }

    public void setSetupComplete(boolean setupComplete) {
        this.setupComplete = setupComplete;
    }

    @Column(name = "name", nullable = false, length = 500)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Column(name = "contact_name", nullable = false, length = 200)
    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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


    @Column(name = "industry_area", nullable = false, length = 100)
    public String getIndustryArea() {
        return this.industryArea;
    }

    public void setIndustryArea(String industryArea) {
        this.industryArea = industryArea;
    }

    @Column(name = "profile", nullable = false, columnDefinition="TEXT")
    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    public Set<Opportunity> getOpportunitieses() {
        return this.opportunitieses;
    }

    public void setOpportunitieses(Set<Opportunity> opportunitieses) {
        this.opportunitieses = opportunitieses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    public List<User> getUserses() {
        return this.userses;
    }

    public void setUserses(List<User> userses) {
        this.userses = userses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    public Set<Endorsement> getEndorsementses() {
        return this.endorsementses;
    }

    public void setEndorsementses(Set<Endorsement> endorsementses) {
        this.endorsementses = endorsementses;
    }
}
