namespace contactmanagertest;



class MyMath
{
    public int Add(int a, int b)
    {
        return a + b;
    }
}

public class UnitTest1
{
    [Fact] //test attribute 
    public void Test1()
    {
        //Arrange (means setting up the test)
        MyMath myMath = new MyMath();
        
        int num1=10,num2=20;
        int expected = 30;
        
        //Act (means performing the test)
        int result = myMath.Add(num1,num2);
        
        //Assert (means checking the result)
        Assert.Equal(expected,result);
    }
}