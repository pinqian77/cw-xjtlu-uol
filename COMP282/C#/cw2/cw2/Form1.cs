using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace cw2
{
    public partial class Form1 : Form
    {
        // Container to store contries
        List<Country> countries = new List<Country>();

        // Active button type
        string btn_type = "A";

        // Selected country name
        string selected_country = "";

        public Form1()
        {
            InitializeComponent();

            // Set ListView propertites
            countryListView.View = View.Details;
            countryListView.FullRowSelect = true;

            // Create columns
            countryListView.Columns.Add("Name ↓", 185);
            countryListView.Columns.Add("Population Size ↓", 185);
        }

        // Disable resize colunm
        private void countryListView_ColumnWidthChanging(object sender, ColumnWidthChangingEventArgs e)
        {
            e.Cancel = true;
            e.NewWidth = countryListView.Columns[e.ColumnIndex].Width;
        }

        // Display countries in the container
        private void displayContryList()
        {
            // Clear current items
            countryListView.Items.Clear();

            //Add items in the listview
            string[] arr = new string[2];
            foreach (Country c in countries)
            {
                arr[0] = c.name;
                arr[1] = Convert.ToString(c.population);
                countryListView.Items.Add(new ListViewItem(arr));
            }
        }

        // Selecte item in the listview
        private void countryListView_SelectedIndexChanged(object sender, EventArgs e)
        {
            // Check if item in listview is selected
            if (countryListView.SelectedItems.Count == 0)
            {
                return;
            }

            // Get selected item
            ListViewItem item = countryListView.SelectedItems[0];
            nameInput.Text = item.SubItems[0].Text;
            popInput.Text = item.SubItems[1].Text;

            // Update global information
            selected_country = item.SubItems[0].Text;
            btn_type = "U";
        }

        // Add item
        private void btnAdd_Click(object sender, EventArgs e)
        {
            try
            {
                // Insert case
                if (countryListView.SelectedItems.Count != 0)
                {
                    // Get index of selected item
                    int idx = countryListView.SelectedItems[0].Index;
                    countries.Insert(idx, new Country(nameInput.Text, long.Parse(popInput.Text)));

                    // Display results
                    displayContryList();

                    // Update global information
                    countryListView.Items[idx].Selected = true;
                    selected_country = countryListView.Items[idx].SubItems[0].Text;
                }
                // Add case
                else
                {
                    // Add the new item
                    countries.Add(new Country(nameInput.Text, long.Parse(popInput.Text)));

                    // Display results
                    displayContryList();

                    // Update global information
                    countryListView.Items[countries.Count - 1].Selected = true;
                    selected_country = countryListView.Items[countries.Count - 1].SubItems[0].Text;
                }

                btn_type = "A";
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message, "Add error");
            }
        }

        // Remove item
        private void btnRemove_Click(object sender, EventArgs e)
        {
            try
            {
                // Check if item in listview is selected
                if (countryListView.SelectedItems.Count == 0)
                {
                    MessageBox.Show("A country must be selected!", "Remove Error");
                    return;
                }

                // Remove the selected item
                int idx = countryListView.SelectedItems[0].Index;
                countries.RemoveAt(idx);

                // Display the results
                displayContryList();

                // Exists item
                if (countries.Count != 0)
                {
                    // Removed the last one
                    if (idx == countries.Count)
                    {
                        countryListView.Items[countries.Count - 1].Selected = true;
                        selected_country = countryListView.Items[countries.Count - 1].SubItems[0].Text;
                    }
                    // Normal case
                    else
                    {
                        countryListView.Items[idx].Selected = true;
                        selected_country = countryListView.Items[idx].SubItems[0].Text;
                    }
                }
                // No item left
                else
                {
                    selected_country = "";
                }

                btn_type = "R";
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message, "Remove Error");
            }
            
        }

        // Update item
        private void btnUpdate_Click(object sender, EventArgs e)
        {
            try
            {
                // Check if item in listview is selected
                if (countryListView.SelectedItems.Count == 0)
                {
                    MessageBox.Show("A country must be selected!", "Update Error");
                    return;
                }

                // Update the selected item
                int idx = countryListView.SelectedItems[0].Index;
                countries[idx].name = nameInput.Text;
                countries[idx].population = long.Parse(popInput.Text);

                // Display the results
                displayContryList();

                // Update global information
                countryListView.Items[idx].Selected = true;
                selected_country = countryListView.Items[idx].SubItems[0].Text;
                btn_type = "U";
            }
            catch (Exception exception)
            {
                MessageBox.Show(exception.Message, "Update Error");
            }
            
        }

        private void enableButton()
        {
            switch (btn_type)
            {
                case "A":
                    ActiveForm.AcceptButton = btnAdd;
                    break;
                case "R":
                    ActiveForm.AcceptButton = btnRemove;
                    break;
                case "U":
                    ActiveForm.AcceptButton = btnUpdate;
                    break;
            }
        }

        private void nameInput_TextChanged(object sender, EventArgs e)
        {
            enableButton();
        }

        private void popInput_TextChanged(object sender, EventArgs e)
        {
            enableButton();
        }

        // Sort items
        private void countryListView_ColumnClick(object sender, ColumnClickEventArgs e)
        {
            IOrderedEnumerable<Country> sortedList;
            List<Country> tmp = new List<Country>();

            // Sort by Name
            if (e.Column == 0)
            {
                if (countryListView.Columns[e.Column].Text == "Name ↓")
                {
                    countryListView.Columns[e.Column].Text = "Name ↑";
                    sortedList = from country in countries
                                     orderby country.name ascending
                                     select country;
                }
                else
                {
                    countryListView.Columns[e.Column].Text = "Name ↓";
                    sortedList = from country in countries
                                     orderby country.name descending
                                     select country;
                }
            }
            // Sort by Pop Size
            else
            {
                if (countryListView.Columns[e.Column].Text == "Population Size ↓")
                {
                    countryListView.Columns[e.Column].Text = "Population Size ↑";
                    sortedList = from country in countries
                                     orderby country.population ascending
                                     select country;
                }
                else
                {
                    countryListView.Columns[e.Column].Text = "Population Size ↓";
                    sortedList = from country in countries
                                     orderby country.population descending
                                     select country;
                }
            }

            // Update the container
            foreach (Country c in sortedList)
            {
                tmp.Add(c);
            }
            countries = tmp;

            // Display the results
            displayContryList();

            // // Update global information
            for (int i = 0; i < countries.Count; i++)
            {
                if (countryListView.Items[i].SubItems[0].Text == selected_country)
                {
                    countryListView.Items[i].Selected = true;
                    break;
                }
            }
        }
    }
}