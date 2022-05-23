#include "country.h"

Country::Country(std::string name, long pop) : name(name), pop(pop){}
Country::Country(): name(""), pop(0){}
Country::~Country(){}

string Country::getName() { return name; }
long Country::getPop() { return pop; }

// Overloaded IO operators <<
ostream& operator<<(ostream& output, const Country& country) {
    output << country.name << " " << country.pop;
    return output;
}

// Overloaded IO operators >>
istream& operator>>(istream& input, Country& country) {
    input >> country.name >> country.pop;
    return input;
}

bool Country::operator<(const Country& country)
{
    return pop < country.pop;
}

bool Country::operator>(const Country& country)
{
    return pop > country.pop;
}

bool Country::operator==(const Country& country)
{
    return pop == country.pop;
}