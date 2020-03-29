public class Proc
{
    private String procLabel;
    private int vt;

    public Proc(String procLabel, int vt)
    {
        this.procLabel = procLabel;
        this.vt = vt;
    }

    public int getVt()
    {
        return vt;
    }

    public String getProcLabel()
    {
        return procLabel;
    }
}