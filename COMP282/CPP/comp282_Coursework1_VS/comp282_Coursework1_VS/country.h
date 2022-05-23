#include <string>
#include <iostream>
using namespace std;

class Country
{
private:
	string name;
	long pop;

public:
	// Constructors and deconstructor
	Country(std::string name, long pop);
	Country();
	~Country();
	 
	// Getters
	string getName();
	long getPop();

	// Overloaded IO operators
	friend std::ostream& operator<<(ostream& output, const Country& country);
	friend std::istream& operator>>(istream& input, Country& country);

	// Overloaded comparison operators
	bool operator>(const Country& country);
	bool operator<(const Country& country);
	bool operator==(const Country& country);
};
