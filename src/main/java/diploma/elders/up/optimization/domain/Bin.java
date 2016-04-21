package diploma.elders.up.optimization.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea Iepure on 4/6/2016.
 */
public class Bin {


    private double value;
    private List<Tuple> result;

    public Bin() {
        this.value = 0;
        this.result = new ArrayList<>();
    }

    public Bin(double value, List<Tuple> result) {
        this.value = value;
        this.result = result;
    }

    public double getValue() {
        return value;
    }

    public List<Tuple> getResult() {
        return result;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setResult(List<Tuple> result) {
        this.result = result;
    }
    public void addTuple(Tuple t)
    {
        result.add(t);
    }

    @Override
    public String toString() {
        return "Bin{" +
                "value=" + value +
                ", result=" + result +
                '}';
    }
}