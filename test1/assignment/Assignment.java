package test1.assignment;

abstract public class Assignment
{
    int percentageOfTotalGrade;
    double grade;
    public Assignment(){}
    public int getPercent()
    {
        return percentageOfTotalGrade;
    }
    public double getGrade()
    {
        return grade;
    }
    public void setPercent(int percent)
    {
        this.percentageOfTotalGrade = percent;
    }
    public void setGrade(double grade)
    {
        this.grade = grade;
    }
}