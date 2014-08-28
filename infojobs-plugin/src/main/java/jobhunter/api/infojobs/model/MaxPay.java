
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
    "amount",
    "amountId",
    "periodId",
    "periodValue",
    "amountValue"
})
public class MaxPay {

    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("amountId")
    private Integer amountId;
    @JsonProperty("periodId")
    private Integer periodId;
    @JsonProperty("periodValue")
    private String periodValue;
    @JsonProperty("amountValue")
    private String amountValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("amountId")
    public Integer getAmountId() {
        return amountId;
    }

    @JsonProperty("amountId")
    public void setAmountId(Integer amountId) {
        this.amountId = amountId;
    }

    @JsonProperty("periodId")
    public Integer getPeriodId() {
        return periodId;
    }

    @JsonProperty("periodId")
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    @JsonProperty("periodValue")
    public String getPeriodValue() {
        return periodValue;
    }

    @JsonProperty("periodValue")
    public void setPeriodValue(String periodValue) {
        this.periodValue = periodValue;
    }

    @JsonProperty("amountValue")
    public String getAmountValue() {
        return amountValue;
    }

    @JsonProperty("amountValue")
    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
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
