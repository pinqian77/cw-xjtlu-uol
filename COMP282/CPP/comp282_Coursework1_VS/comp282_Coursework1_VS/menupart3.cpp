#include "menupart3.h"
#include "country.h"
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;


bool comp(Country c1, Country c2) {
    return c1.getPop() < c2.getPop();
}

void MenuPart3::menu()
{
    vector<Country> vec;

    // MAIN MENU LOOP
    char op;
    do {
        cout << "1. Add country\n2. List countries\n3. Find largest country\n4. Remove country\n5. Sort countries\nQ. Quit" << endl;
        cout << "Enter Option: " << endl;
        cin >> op;
        if (op == '1') {
            cout << "Enter Details: " << endl;
            Country c;
            cin >> c;
            vec.push_back(c);
        }
        else if (op == '2') {
            int i = 1;
            for (Country c : vec) {
                cout << "[" << i << "] " << c.getName() << " " << c.getPop() << endl;
                i++;
            }
        }
        else if (op == '3') {
            if (vec.size() == 0) {
                cout << "There are no countries in the index currently" << endl;
            }
            else {
                Country largest = *max_element(vec.begin(), vec.end(), comp);
                cout << "Largest: " << largest.getName() << " " << largest.getPop() << endl;
            }
        }
        else if (op == '4') {
            cout << "Enter Index: " << endl;
            int idx;
            cin >> idx;

            if (idx < 1 || idx > vec.size()) {
                cout << "No country has that index" << endl;
            }
            else {
                vec.erase(vec.begin() + idx - 1, vec.begin() + idx);
            }
        }
        else if (op == '5') {
            sort(vec.begin(), vec.end(), comp);
        }
    } while (op != 'q' && op != 'Q');
}
