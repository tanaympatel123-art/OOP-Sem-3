abstract class Employee
{
    String name;
    int id;
    Employee(String name,int id)
    {
        this.name=name;
        this.id=id;
    }

    abstract double monthlySalary();
}

class Fulltime extends Employee
{
    double salary;
    Fulltime(String name, int id,double salary)
    {
        super(name,id);
        this.salary=salary;
    }

    @Override
    double monthlySalary()
    {
        return salary;
    }
}

class Parttime extends Employee
{
    int hours;
    double rate;
    Parttime(String name,int id,int hours,double rate)
    {
        super(name, id);
        this.hours=hours;
        this.rate=rate;
    }

    @Override
    double monthlySalary()
    {
        return (hours*rate);
    }
}

class Intern extends Employee
{
    double stipend;
    Intern(String name, int id,double stipend)
    {
        super(name, id);
        this.stipend=stipend;
    }

    @Override
    double monthlySalary()
    {
        return stipend;
    }
}

public class Salary {
        public static void main(String[] args)
        {
            Fulltime f = new Fulltime("Tanay", 101, 20000);
            Parttime p =new Parttime("Rakesh",102,15, 25.50);
            Intern i = new Intern("Mahendra", 103, 5000);

            Employee[] e = new Employee[3];
            e[0]=f;
            e[1]=p;
            e[2]=i;

            double total=0;

            for(int j=0;j<3;j++)
            {
                double current;
                current=e[j].monthlySalary();
                total+=current;

                System.out.println("The salary is: "+current);
                if(e[j] instanceof Intern)
                {
                    System.out.println("This message is for interns only."+"\n");
                }
            }

            System.out.println("The total salary expenditure is: "+total);
        }
}
