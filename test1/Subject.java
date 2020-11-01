package test1;

import test1.assignment.Assignment;

public class Subject 
{
    String name;
    int credit = 0;
    Assignment[] allAssignments = new Assignment[5];
    double totalGrade = 0.0;

    public Subject(String name)
    {
        this.name = name;
    }
    public void setCredit(int credit)
    {
        this.credit = credit;
    }
    
    public void addAssignment(Assignment asmt)
    {
        for (int i = 0; i < allAssignments.length; i++) {
            if(allAssignments[allAssignments.length-1] != null)
            {
                doubleLength();
            }
            if(allAssignments[i] == null)
            {
                allAssignments[i] = asmt;
                totalGrade = totalGrade + ((allAssignments[i].getPercent()/100) * allAssignments[i].getGrade());
                break;
            }
        }
    }



    public void doubleLength()
    {
        Assignment[] doubleTheSize = new Assignment[this.allAssignments.length*2];
        for (int i = 0; i < this.allAssignments.length; i++) {
            doubleTheSize[i] = this.allAssignments[i];
        }
        this.allAssignments = doubleTheSize;
    }
}