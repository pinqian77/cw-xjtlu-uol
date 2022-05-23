
namespace cw2
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnAdd = new System.Windows.Forms.Button();
            this.btnRemove = new System.Windows.Forms.Button();
            this.btnUpdate = new System.Windows.Forms.Button();
            this.nameInput = new System.Windows.Forms.TextBox();
            this.nameLabel = new System.Windows.Forms.Label();
            this.populationLabel = new System.Windows.Forms.Label();
            this.popInput = new System.Windows.Forms.TextBox();
            this.countryListView = new System.Windows.Forms.ListView();
            this.SuspendLayout();
            // 
            // btnAdd
            // 
            this.btnAdd.Location = new System.Drawing.Point(51, 105);
            this.btnAdd.Name = "btnAdd";
            this.btnAdd.Size = new System.Drawing.Size(93, 34);
            this.btnAdd.TabIndex = 1;
            this.btnAdd.Text = "Add";
            this.btnAdd.UseVisualStyleBackColor = true;
            this.btnAdd.Click += new System.EventHandler(this.btnAdd_Click);
            // 
            // btnRemove
            // 
            this.btnRemove.Location = new System.Drawing.Point(163, 105);
            this.btnRemove.Name = "btnRemove";
            this.btnRemove.Size = new System.Drawing.Size(92, 34);
            this.btnRemove.TabIndex = 2;
            this.btnRemove.Text = "Remove";
            this.btnRemove.UseVisualStyleBackColor = true;
            this.btnRemove.Click += new System.EventHandler(this.btnRemove_Click);
            // 
            // btnUpdate
            // 
            this.btnUpdate.Location = new System.Drawing.Point(337, 105);
            this.btnUpdate.Name = "btnUpdate";
            this.btnUpdate.Size = new System.Drawing.Size(114, 34);
            this.btnUpdate.TabIndex = 3;
            this.btnUpdate.Text = "Update";
            this.btnUpdate.UseVisualStyleBackColor = true;
            this.btnUpdate.Click += new System.EventHandler(this.btnUpdate_Click);
            // 
            // nameInput
            // 
            this.nameInput.Location = new System.Drawing.Point(51, 56);
            this.nameInput.Name = "nameInput";
            this.nameInput.Size = new System.Drawing.Size(204, 30);
            this.nameInput.TabIndex = 4;
            this.nameInput.TextChanged += new System.EventHandler(this.nameInput_TextChanged);
            // 
            // nameLabel
            // 
            this.nameLabel.AutoSize = true;
            this.nameLabel.Location = new System.Drawing.Point(51, 29);
            this.nameLabel.Name = "nameLabel";
            this.nameLabel.Size = new System.Drawing.Size(62, 24);
            this.nameLabel.TabIndex = 5;
            this.nameLabel.Text = "Name";
            // 
            // populationLabel
            // 
            this.populationLabel.AutoSize = true;
            this.populationLabel.Location = new System.Drawing.Point(297, 29);
            this.populationLabel.Name = "populationLabel";
            this.populationLabel.Size = new System.Drawing.Size(143, 24);
            this.populationLabel.TabIndex = 7;
            this.populationLabel.Text = "Population Size";
            // 
            // popInput
            // 
            this.popInput.Location = new System.Drawing.Point(297, 56);
            this.popInput.Name = "popInput";
            this.popInput.Size = new System.Drawing.Size(154, 30);
            this.popInput.TabIndex = 6;
            this.popInput.TextChanged += new System.EventHandler(this.popInput_TextChanged);
            // 
            // countryListView
            // 
            this.countryListView.FullRowSelect = true;
            this.countryListView.HideSelection = false;
            this.countryListView.Location = new System.Drawing.Point(51, 161);
            this.countryListView.Name = "countryListView";
            this.countryListView.Size = new System.Drawing.Size(400, 188);
            this.countryListView.TabIndex = 8;
            this.countryListView.UseCompatibleStateImageBehavior = false;
            this.countryListView.ColumnClick += new System.Windows.Forms.ColumnClickEventHandler(this.countryListView_ColumnClick);
            this.countryListView.ColumnWidthChanging += new System.Windows.Forms.ColumnWidthChangingEventHandler(this.countryListView_ColumnWidthChanging);
            this.countryListView.SelectedIndexChanged += new System.EventHandler(this.countryListView_SelectedIndexChanged);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(11F, 24F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(487, 384);
            this.Controls.Add(this.countryListView);
            this.Controls.Add(this.populationLabel);
            this.Controls.Add(this.popInput);
            this.Controls.Add(this.nameLabel);
            this.Controls.Add(this.nameInput);
            this.Controls.Add(this.btnUpdate);
            this.Controls.Add(this.btnRemove);
            this.Controls.Add(this.btnAdd);
            this.Name = "Form1";
            this.Text = "Country App";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion
        private System.Windows.Forms.Button btnAdd;
        private System.Windows.Forms.Button btnRemove;
        private System.Windows.Forms.Button btnUpdate;
        private System.Windows.Forms.TextBox nameInput;
        private System.Windows.Forms.Label nameLabel;
        private System.Windows.Forms.Label populationLabel;
        private System.Windows.Forms.TextBox popInput;
        private System.Windows.Forms.ListView countryListView;
    }
}

