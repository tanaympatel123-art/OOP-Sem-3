abstract class Shape
{
    abstract double area();
}

class Circle extends Shape
{
    double r;
    Circle(double r)
    {
        this.r=r;
    }
    @Override
    double area()
    {
        System.out.println("Area of Circle is: "+(3.14*(r*r)));
        return (3.14*(r*r));
    }
}

class Triangle extends Shape
{
    double b;
    double h;
    Triangle(double b, double h)
    {
        this.b=b;
        this.h=h;
    }
    @Override
    double area()
    {
        System.out.println("Area of Triangle is: "+ (0.5*(b*h)));
        return (0.5*(b*h));
    }
}

class Rectangle extends Shape
{
    double l;
    double b;
    Rectangle(double l, double b)
    {
        this.l=l;
        this.b=b;
    }
    @Override
    double area()
    {
        System.out.println("Area of Rectangle is: "+ (l*b));
        return (l*b);
    }
}

public class Area
{
    public static void main(String[] args)
    {
        Circle c = new Circle(12);
        Triangle t = new Triangle(10,45);
        Rectangle r = new Rectangle(25,75);

        Shape[] s = new Shape[3];
        s[0]=c;
        s[1]=t;
        s[2]=r;

        double largest = 0;
        

    for(int i = 0; i < 3; i++)
    {
        double current = s[i].area();

        if(current > largest)
        {
        
         largest = current;
        }
}

System.out.println("Largest area = " + largest);
    }
}