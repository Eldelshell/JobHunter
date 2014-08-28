
package jobhunter.api.infojobs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "title",
    "id",
    "state",
    "creationDate",
    "updateDate",
    "city",
    "externalUrlForm",
    "blocked",
    "applications",
    "province",
    "experienceMin",
    "category",
    "subcategories",
    "studiesMin",
    "residence",
    "country",
    "contractType",
    "journey",
    "profile",
    "cityPD",
    "zipCode",
    "latitude",
    "longitude",
    "exactLocation",
    "department",
    "vacancies",
    "minRequirements",
    "description",
    "desiredRequirements",
    "commissions",
    "contractDuration",
    "referenceId",
    "detailedStudiesId",
    "studying",
    "showPay",
    "maxPay",
    "minPay",
    "schedule",
    "jobLevel",
    "staffInCharge",
    "hasKillerQuestions",
    "hasOpenQuestions",
    "upsellings",
    "link",
    "active",
    "archived",
    "deleted",
    "disponibleForFullVisualization",
    "availableForVisualization"
})
public class Offer {

    @JsonProperty("title")
    private String title;
    @JsonProperty("id")
    private String id;
    @JsonProperty("state")
    private Integer state;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("updateDate")
    private String updateDate;
    @JsonProperty("city")
    private String city;
    @JsonProperty("externalUrlForm")
    private String externalUrlForm;
    @JsonProperty("blocked")
    private Boolean blocked;
    @JsonProperty("applications")
    private Integer applications;
    @JsonProperty("province")
    private Province province;
    @JsonProperty("experienceMin")
    private ExperienceMin experienceMin;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("subcategories")
    private List<Subcategory> subcategories = new ArrayList<Subcategory>();
    @JsonProperty("studiesMin")
    private StudiesMin studiesMin;
    @JsonProperty("residence")
    private Residence residence;
    @JsonProperty("country")
    private Country country;
    @JsonProperty("contractType")
    private ContractType contractType;
    @JsonProperty("journey")
    private Journey journey;
    @JsonProperty("profile")
    private Profile profile;
    @JsonProperty("cityPD")
    private Integer cityPD;
    @JsonProperty("zipCode")
    private String zipCode;
    @JsonProperty("latitude")
    private Integer latitude;
    @JsonProperty("longitude")
    private Integer longitude;
    @JsonProperty("exactLocation")
    private Boolean exactLocation;
    @JsonProperty("department")
    private String department;
    @JsonProperty("vacancies")
    private Integer vacancies;
    @JsonProperty("minRequirements")
    private String minRequirements;
    @JsonProperty("description")
    private String description;
    @JsonProperty("desiredRequirements")
    private String desiredRequirements;
    @JsonProperty("commissions")
    private String commissions;
    @JsonProperty("contractDuration")
    private String contractDuration;
    @JsonProperty("referenceId")
    private String referenceId;
    @JsonProperty("detailedStudiesId")
    private Integer detailedStudiesId;
    @JsonProperty("studying")
    private Boolean studying;
    @JsonProperty("showPay")
    private Boolean showPay;
    @JsonProperty("maxPay")
    private MaxPay maxPay;
    @JsonProperty("minPay")
    private MinPay minPay;
    @JsonProperty("schedule")
    private String schedule;
    @JsonProperty("jobLevel")
    private JobLevel jobLevel;
    @JsonProperty("staffInCharge")
    private StaffInCharge staffInCharge;
    @JsonProperty("hasKillerQuestions")
    private Integer hasKillerQuestions;
    @JsonProperty("hasOpenQuestions")
    private Integer hasOpenQuestions;
    @JsonProperty("upsellings")
    private Upsellings upsellings;
    @JsonProperty("link")
    private String link;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("archived")
    private Boolean archived;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("disponibleForFullVisualization")
    private Boolean disponibleForFullVisualization;
    @JsonProperty("availableForVisualization")
    private Boolean availableForVisualization;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("state")
    public Integer getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(Integer state) {
        this.state = state;
    }

    @JsonProperty("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("updateDate")
    public String getUpdateDate() {
        return updateDate;
    }

    @JsonProperty("updateDate")
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("externalUrlForm")
    public String getExternalUrlForm() {
        return externalUrlForm;
    }

    @JsonProperty("externalUrlForm")
    public void setExternalUrlForm(String externalUrlForm) {
        this.externalUrlForm = externalUrlForm;
    }

    @JsonProperty("blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @JsonProperty("blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @JsonProperty("applications")
    public Integer getApplications() {
        return applications;
    }

    @JsonProperty("applications")
    public void setApplications(Integer applications) {
        this.applications = applications;
    }

    @JsonProperty("province")
    public Province getProvince() {
        return province;
    }

    @JsonProperty("province")
    public void setProvince(Province province) {
        this.province = province;
    }

    @JsonProperty("experienceMin")
    public ExperienceMin getExperienceMin() {
        return experienceMin;
    }

    @JsonProperty("experienceMin")
    public void setExperienceMin(ExperienceMin experienceMin) {
        this.experienceMin = experienceMin;
    }

    @JsonProperty("category")
    public Category getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonProperty("subcategories")
    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    @JsonProperty("subcategories")
    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    @JsonProperty("studiesMin")
    public StudiesMin getStudiesMin() {
        return studiesMin;
    }

    @JsonProperty("studiesMin")
    public void setStudiesMin(StudiesMin studiesMin) {
        this.studiesMin = studiesMin;
    }

    @JsonProperty("residence")
    public Residence getResidence() {
        return residence;
    }

    @JsonProperty("residence")
    public void setResidence(Residence residence) {
        this.residence = residence;
    }

    @JsonProperty("country")
    public Country getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(Country country) {
        this.country = country;
    }

    @JsonProperty("contractType")
    public ContractType getContractType() {
        return contractType;
    }

    @JsonProperty("contractType")
    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    @JsonProperty("journey")
    public Journey getJourney() {
        return journey;
    }

    @JsonProperty("journey")
    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    @JsonProperty("profile")
    public Profile getProfile() {
        return profile;
    }

    @JsonProperty("profile")
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @JsonProperty("cityPD")
    public Integer getCityPD() {
        return cityPD;
    }

    @JsonProperty("cityPD")
    public void setCityPD(Integer cityPD) {
        this.cityPD = cityPD;
    }

    @JsonProperty("zipCode")
    public String getZipCode() {
        return zipCode;
    }

    @JsonProperty("zipCode")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("latitude")
    public Integer getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Integer getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("exactLocation")
    public Boolean getExactLocation() {
        return exactLocation;
    }

    @JsonProperty("exactLocation")
    public void setExactLocation(Boolean exactLocation) {
        this.exactLocation = exactLocation;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    @JsonProperty("vacancies")
    public Integer getVacancies() {
        return vacancies;
    }

    @JsonProperty("vacancies")
    public void setVacancies(Integer vacancies) {
        this.vacancies = vacancies;
    }

    @JsonProperty("minRequirements")
    public String getMinRequirements() {
        return minRequirements;
    }

    @JsonProperty("minRequirements")
    public void setMinRequirements(String minRequirements) {
        this.minRequirements = minRequirements;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("desiredRequirements")
    public String getDesiredRequirements() {
        return desiredRequirements;
    }

    @JsonProperty("desiredRequirements")
    public void setDesiredRequirements(String desiredRequirements) {
        this.desiredRequirements = desiredRequirements;
    }

    @JsonProperty("commissions")
    public String getCommissions() {
        return commissions;
    }

    @JsonProperty("commissions")
    public void setCommissions(String commissions) {
        this.commissions = commissions;
    }

    @JsonProperty("contractDuration")
    public String getContractDuration() {
        return contractDuration;
    }

    @JsonProperty("contractDuration")
    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    @JsonProperty("referenceId")
    public String getReferenceId() {
        return referenceId;
    }

    @JsonProperty("referenceId")
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @JsonProperty("detailedStudiesId")
    public Integer getDetailedStudiesId() {
        return detailedStudiesId;
    }

    @JsonProperty("detailedStudiesId")
    public void setDetailedStudiesId(Integer detailedStudiesId) {
        this.detailedStudiesId = detailedStudiesId;
    }

    @JsonProperty("studying")
    public Boolean getStudying() {
        return studying;
    }

    @JsonProperty("studying")
    public void setStudying(Boolean studying) {
        this.studying = studying;
    }

    @JsonProperty("showPay")
    public Boolean getShowPay() {
        return showPay;
    }

    @JsonProperty("showPay")
    public void setShowPay(Boolean showPay) {
        this.showPay = showPay;
    }

    @JsonProperty("maxPay")
    public MaxPay getMaxPay() {
        return maxPay;
    }

    @JsonProperty("maxPay")
    public void setMaxPay(MaxPay maxPay) {
        this.maxPay = maxPay;
    }

    @JsonProperty("minPay")
    public MinPay getMinPay() {
        return minPay;
    }

    @JsonProperty("minPay")
    public void setMinPay(MinPay minPay) {
        this.minPay = minPay;
    }

    @JsonProperty("schedule")
    public String getSchedule() {
        return schedule;
    }

    @JsonProperty("schedule")
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @JsonProperty("jobLevel")
    public JobLevel getJobLevel() {
        return jobLevel;
    }

    @JsonProperty("jobLevel")
    public void setJobLevel(JobLevel jobLevel) {
        this.jobLevel = jobLevel;
    }

    @JsonProperty("staffInCharge")
    public StaffInCharge getStaffInCharge() {
        return staffInCharge;
    }

    @JsonProperty("staffInCharge")
    public void setStaffInCharge(StaffInCharge staffInCharge) {
        this.staffInCharge = staffInCharge;
    }

    @JsonProperty("hasKillerQuestions")
    public Integer getHasKillerQuestions() {
        return hasKillerQuestions;
    }

    @JsonProperty("hasKillerQuestions")
    public void setHasKillerQuestions(Integer hasKillerQuestions) {
        this.hasKillerQuestions = hasKillerQuestions;
    }

    @JsonProperty("hasOpenQuestions")
    public Integer getHasOpenQuestions() {
        return hasOpenQuestions;
    }

    @JsonProperty("hasOpenQuestions")
    public void setHasOpenQuestions(Integer hasOpenQuestions) {
        this.hasOpenQuestions = hasOpenQuestions;
    }

    @JsonProperty("upsellings")
    public Upsellings getUpsellings() {
        return upsellings;
    }

    @JsonProperty("upsellings")
    public void setUpsellings(Upsellings upsellings) {
        this.upsellings = upsellings;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("archived")
    public Boolean getArchived() {
        return archived;
    }

    @JsonProperty("archived")
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("disponibleForFullVisualization")
    public Boolean getDisponibleForFullVisualization() {
        return disponibleForFullVisualization;
    }

    @JsonProperty("disponibleForFullVisualization")
    public void setDisponibleForFullVisualization(Boolean disponibleForFullVisualization) {
        this.disponibleForFullVisualization = disponibleForFullVisualization;
    }

    @JsonProperty("availableForVisualization")
    public Boolean getAvailableForVisualization() {
        return availableForVisualization;
    }

    @JsonProperty("availableForVisualization")
    public void setAvailableForVisualization(Boolean availableForVisualization) {
        this.availableForVisualization = availableForVisualization;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
