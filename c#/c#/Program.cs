class Sample
{
	static void Main(string[] args)
	{

		Console.WriteLine("Hello World");
		Console.Clear();//clears the terminal
		string? str = Console.ReadLine();//read from terminal

		//varibles
		//bytes
		byte b = byte.MinValue;
		sbyte sb = 10;

		//short
		short s = 100;
		ushort us = ushort.MaxValue;

		//int
		int i = int.MaxValue;
		uint ui = 12222;

		//long
		long l = -98989427842789;
		ulong ul = 9482842;

		//foat
		float f = float.MaxValue;
		//double
		double d = 2.0d;

		//char
		char c = 'd';

		//string
		string str1 = "Hello world";

		//boolean
		bool isTrue = true;

		//assienment operator =
		var id = 12322;

		var add = 2 + 2; //addition ( -, *, /, % )
		var name = "Akhlaq" + "Ahmad"; //string concatination

		//increment and decriment oprators
		var t = 10;
		var dec = t++;//(++t,t--,--t)

		//comparison operators
		var ans1 = 10 > 12;//(>,>=,<,<=,==,!=)

		//logcal operator
		var ans3 = true && false;//(&&, ||, !)

		//ternaray operator
		var age = 10;
		var ans4 = age >= 18 ? "adult" : age >= 12 ? "child" : "teen";



	}
}
