
package jobhunter.api.infojobs.model;

import java.util.HashMap;
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
    "id",
    "name",
    "description",
    "province",
    "web",
    "numberWorkers",
    "url",
    "corporateWebsiteUrl",
    "websiteUrl",
    "hidden"
})
public class Profile {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("province")
    private Province_ province;
    @JsonProperty("web")
    private String web;
    @JsonProperty("numberWorkers")
    private Integer numberWorkers;
    @JsonProperty("url")
    private String url;
    @JsonProperty("corporateWebsiteUrl")
    private String corporateWebsiteUrl;
    @JsonProperty("websiteUrl")
    private String websiteUrl;
    @JsonProperty("hidden")
    private Boolean hidden;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("province")
    public Province_ getProvince() {
        return province;
    }

    @JsonProperty("province")
    public void setProvince(Province_ province) {
        this.province = province;
    }

    @JsonProperty("web")
    public String getWeb() {
        return web;
    }

    @JsonProperty("web")
    public void setWeb(String web) {
        this.web = web;
    }

    @JsonProperty("numberWorkers")
    public Integer getNumberWorkers() {
        return numberWorkers;
    }

    @JsonProperty("numberWorkers")
    public void setNumberWorkers(Integer numberWorkers) {
        this.numberWorkers = numberWorkers;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("corporateWebsiteUrl")
    public String getCorporateWebsiteUrl() {
        return corporateWebsiteUrl;
    }

    @JsonProperty("corporateWebsiteUrl")
    public void setCorporateWebsiteUrl(String corporateWebsiteUrl) {
        this.corporateWebsiteUrl = corporateWebsiteUrl;
    }

    @JsonProperty("websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    @JsonProperty("websiteUrl")
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty("hidden")
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
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
