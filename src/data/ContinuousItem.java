package data;

public class ContinuousItem extends Item
{
    private Attribute attribute;
    private double value;

    public ContinuousItem(Attribute attribute, double value)
    {
        super(attribute, value);
    }

    @Override
    public double distance(Object a)
    {
        if (a == null) 
            throw new IllegalArgumentException("Il valore specificato non può essere null.");
            
        if (getValue() == null)
            throw new IllegalArgumentException("Il valore dell'item non può essere null.");

        if (!getValue().getClass().equals(a.getClass()))
            throw new IllegalArgumentException("Il valore specificato deve essere dello stesso tipo del valore dell'item.");

        return Math.abs(
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) a) - 
            ((ContinuousAttribute) getAttribute()).getScaledValue((Double) getValue()) 
        );
    }
}