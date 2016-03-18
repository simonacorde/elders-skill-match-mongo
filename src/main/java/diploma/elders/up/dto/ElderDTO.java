package diploma.elders.up.dto;

import diploma.elders.up.dao.entity.Elder;

import java.util.List;

/**
 * Created by Simonas on 3/2/2016.
 */
public class ElderDTO {

    private Elder elder;
    private double matchingPercentage;
    private List<OpportunityDTO> matchingOffers;

    public ElderDTO(Elder elder) {
        this.elder = elder;
    }

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }

    public List<OpportunityDTO> getMatchingOffers() {
        return matchingOffers;
    }

    public void setMatchingOffers(List<OpportunityDTO> matchingOffers) {
        this.matchingOffers = matchingOffers;
    }

    public Elder getElder() {
        return elder;
    }

    @Override
    public String toString() {
        return "ElderDTO{" +
                "elder=" + elder +
                ", matchingPercentage=" + matchingPercentage +
                ", matchingOffers=" + matchingOffers +
                '}';
    }
}
