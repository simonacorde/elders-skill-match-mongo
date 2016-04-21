package diploma.elders.up.optimization.domain;

import diploma.elders.up.dto.ElderDTO;

/**
 * Created by Andreea Iepure on 4/6/2016.
 */
///Used in case of multiple bins approch
public class Tuple {

    private ElderDTO elder;
    private boolean isInBin;

    public Tuple(ElderDTO elder, boolean isInBin) {
        this.elder = elder;
        this.isInBin = isInBin;
    }

    public ElderDTO getElder() {
        return elder;
    }

    public boolean isInBin() {
        return isInBin;
    }

    public void setElder(ElderDTO elder) {
        this.elder = elder;
    }

    public void setIsInBin(boolean isInBin) {
        this.isInBin = isInBin;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "elder=" + elder +
                ", isInBin=" + isInBin +
                '}';
    }
}