#include "menupart2.h"
#include "country.h"
#include <iostream>
using namespace std;

void MenuPart2::menu()
{
    const int size = 9;
    Country arr[size];
    int count = 0;

    // MAIN MENU LOOP
    char op;
    do {
        cout << "1. Add country\n2. List countries\n3. Find largest country\n4. Remove country\nQ. Quit" << endl;
        cout << "Enter Option: " << endl;
        cin >> op;
        if (op == '1') {
            if (count == size) {
                cout << "Can not add country: There are already " << size << " countries" << endl;
            }
            else {
                cout << "Enter Details: " << endl;
                Country c;
                cin >> c;

                arr[count++] = c;
            }
        }
        else if (op == '2') {
            for (int i = 0; i < count; i++) {
                cout << "[" << i+1 << "] " << arr[i].getName() << " " << arr[i].getPop() << endl;
            }
        }
        else if (op == '3') {
            if (count == 0) {
                cout << "There are no countries in the index currently" << endl;
            }
            else {
                Country largest = arr[0];
                for (int i = 0; i < count; i++) {
                    if (arr[i].getPop() > largest.getPop()) {
                        largest = arr[i];
                    }
                }
                cout << "Largest: " << largest.getName() << " " << largest.getPop() << endl;
            }
        }
        else if (op == '4') {
            cout << "Enter Index: " << endl;
            int idx;
            cin >> idx;

            if (idx < 1 || idx > count){
                cout << "No country has that index" << endl;
            }
            else {
                int num = count - idx;
                while (num > 0) {
                    arr[idx - 1] = arr[idx];
                    idx++;
                    num--;
                }
                count--;
            }
        }
    } while (op != 'q' && op != 'Q');
}
