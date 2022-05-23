using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace cw2
{
    class Country
    {
        public string name { get; set; }
        public long population { get; set; }

        public Country(string name, long population)
        {
            this.name = name;
            this.population = population;
        }

        public override string ToString()
        {
            return name + " " + population;
        }
    }
}
