
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
    "highlightColor",
    "highlightUrgent",
    "highlightHomeMonth",
    "highlightHomeWeek",
    "highlightSubcategory",
    "highlightLogo",
    "highlightStandingOffer"
})
public class Upsellings {

    @JsonProperty("highlightColor")
    private Boolean highlightColor;
    @JsonProperty("highlightUrgent")
    private Boolean highlightUrgent;
    @JsonProperty("highlightHomeMonth")
    private Boolean highlightHomeMonth;
    @JsonProperty("highlightHomeWeek")
    private Boolean highlightHomeWeek;
    @JsonProperty("highlightSubcategory")
    private Boolean highlightSubcategory;
    @JsonProperty("highlightLogo")
    private Boolean highlightLogo;
    @JsonProperty("highlightStandingOffer")
    private Boolean highlightStandingOffer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("highlightColor")
    public Boolean getHighlightColor() {
        return highlightColor;
    }

    @JsonProperty("highlightColor")
    public void setHighlightColor(Boolean highlightColor) {
        this.highlightColor = highlightColor;
    }

    @JsonProperty("highlightUrgent")
    public Boolean getHighlightUrgent() {
        return highlightUrgent;
    }

    @JsonProperty("highlightUrgent")
    public void setHighlightUrgent(Boolean highlightUrgent) {
        this.highlightUrgent = highlightUrgent;
    }

    @JsonProperty("highlightHomeMonth")
    public Boolean getHighlightHomeMonth() {
        return highlightHomeMonth;
    }

    @JsonProperty("highlightHomeMonth")
    public void setHighlightHomeMonth(Boolean highlightHomeMonth) {
        this.highlightHomeMonth = highlightHomeMonth;
    }

    @JsonProperty("highlightHomeWeek")
    public Boolean getHighlightHomeWeek() {
        return highlightHomeWeek;
    }

    @JsonProperty("highlightHomeWeek")
    public void setHighlightHomeWeek(Boolean highlightHomeWeek) {
        this.highlightHomeWeek = highlightHomeWeek;
    }

    @JsonProperty("highlightSubcategory")
    public Boolean getHighlightSubcategory() {
        return highlightSubcategory;
    }

    @JsonProperty("highlightSubcategory")
    public void setHighlightSubcategory(Boolean highlightSubcategory) {
        this.highlightSubcategory = highlightSubcategory;
    }

    @JsonProperty("highlightLogo")
    public Boolean getHighlightLogo() {
        return highlightLogo;
    }

    @JsonProperty("highlightLogo")
    public void setHighlightLogo(Boolean highlightLogo) {
        this.highlightLogo = highlightLogo;
    }

    @JsonProperty("highlightStandingOffer")
    public Boolean getHighlightStandingOffer() {
        return highlightStandingOffer;
    }

    @JsonProperty("highlightStandingOffer")
    public void setHighlightStandingOffer(Boolean highlightStandingOffer) {
        this.highlightStandingOffer = highlightStandingOffer;
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
