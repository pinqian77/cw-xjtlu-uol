// Variables for game
var grid_row = 10;
var grid_col = 10;
var grid = createGrid(grid_row, grid_col);
var stage = "set";
var round = 1;
var num_u_ship = 0;
var num_r_ship = 0;
var num_i_mine = 0;
var arr_r = [];
var arr_m = [];
var arr_a = [];
var surroundings = [
  [-1, -1],
  [-1, 0],
  [-1, 1],
  [0, 1],
  [0, -1],
  [1, 1],
  [1, 0],
  [1, -1],
];

function Ship(row, col) {
  this.row = row;
  this.col = col;
}

function Asteroid(row, col) {
  this.row = row;
  this.col = col;
}

function Mine(row, col) {
  this.row = row;
  this.col = col;
  this.isActive = false;
}

var u_ship = new Ship(0, 0);

/**
 * Display messages on the page
 * @param id the id of the HTML tag element
 * @param message the content to be displayed
 */
function displayMessage(id, message) {
  document.getElementById(id).innerHTML = message;
}

/**
 * Display game infomation
 */
function displayInfo() {
  var info = "Round: " + "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp;" + round + "<br/>";
  info += "Inactive Mine: " + "&emsp;&emsp;&ensp;&nbsp;&ensp;" + num_i_mine + "<br/>";
  info += "Robotics Spaceship: " + "&ensp;" + num_r_ship + "<br/>";
  displayMessage("game_info", info);
}

/**
 * Create resizable grid
 * @param row the number of row of the grid
 * @param col the number of col of the grid
 */
function createGrid(row, col) {
  var arr = new Array(row);
  for (var i = 0; i < row; i++) {
    var sub_arr = new Array(col);
    for (var j = 0; j < col; j++) {
      sub_arr[j] = " ";
    }
    arr[i] = sub_arr;
  }
  return arr;
}

/**
 * Set cell value of the grid.
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param event event triggered by clicking on the cell
 */
function createCell(x, y, event) {
  // Only set stage can create cell
  if (stage == "set") {
    // The chosen position has been occupied
    if (grid[x][y] != " ") {
      displayMessage("alert_info", "Position [" + x + "," + y + "] has been occupied!");
    }
    // Valid to palce new object
    else {
      var input = prompt("You are placing an object in cell [" + x + "," + y + "]:");

      // Process valid input
      if (input == "u" || input == "r" || input == "a" || input == "m") {
        // Try to place an user ship
        if (input == "u") {
          if (num_u_ship > 0) {
            displayMessage("alert_info", "Only one user spaceship is allowed!");
          } 
          else {
            grid[x][y] = input;
            event.target.innerHTML = charToImg(input);
            u_ship.row = x;
            u_ship.col = y;
            num_u_ship++;
          }
        }
        // Try to place a robotic ship
        else if (input == "r") {
          grid[x][y] = input;
          event.target.innerHTML = charToImg(input);
          num_r_ship++;
          arr_r.push(new Ship(x, y));
        }
        // Try to place an asteroid
        else if (input == "a") {
          grid[x][y] = input;
          event.target.innerHTML = charToImg(input);
          arr_a.push(new Asteroid(x, y));
        }
        // Try to place a mine
        else if (input == "m") {
          grid[x][y] = input;
          event.target.innerHTML = charToImg(input);
          num_i_mine++;
          arr_m.push(new Mine(x, y));
        }
      }
      // Invalid input
      else {
        if (input != null) displayMessage("alert_info", "Invalid Object!");
      }
    }
  }
}

/**
 * Convert char to letter.
 * @param char the character of the cell
 */
function charToImg(char) {
  switch (char) {
    case " ":
      return " ";
    case "a":
      return "<img src='assets/A.png'  alt='A'/>";
    case "r":
      return "<img src='assets/R.png'  alt='R'/>";
    case "m":
      return "<img src='assets/UM.png'  alt='M'/>";
    case "active_m":
      return "<img src='assets/AM.png'  alt='AM'/>";
    case "u":
    case "hidden_m":
      return "<img src='assets/U.png'  alt='U'/>";
  }
}

/**
 * Initialize the grid table.
 */
function initialize() {
  // Set button visibility status and initialize page
  document.getElementById("moveUserBtn").style.visibility = "hidden";
  document.getElementById("direction").style.visibility = "hidden";
  document.getElementById("exitBtn").style.visibility = "hidden";
  document.getElementById("restartBtn").style.visibility = "hidden";
  displayMessage("stage", "Click the cell and set up the grid!");
  displayMessage("alert_info", "&nbsp;");
  displayMessage("guide_info", "Set up by clicking cell and entering letter:<br/> 'a' &nbsp;&nbsp;--> Asteroid <br/>'m' &nbsp;--> Mine <br/>'r' &nbsp;&nbsp;&nbsp;--> Robotic spaceship <br/>'u' &nbsp;&nbsp;--> Your spaceship");

  // Create cells
  for (x = 0; x < grid.length; x++) {
    var tr = document.createElement("tr");
    document.getElementById("grid").appendChild(tr);
    for (y = 0; y < grid[x].length; y++) {
      var td = document.createElement("td");
      var txt = document.createTextNode(" ");
      td.appendChild(txt);
      td.setAttribute("id", x.toString() + y.toString());
      td.addEventListener("click", createCell.bind(null, x, y), false);
      tr.appendChild(td);
    }
  }
}

/**
 * Check if reach the game end
 */
 function checkEnd() {
  return (num_u_ship == 0 || num_r_ship == 0 || num_i_mine == 0 || !isAbleToMove());
}

/**
 *  Check if the grid setting is valid
 */
function checkSetup() {
  // If there is no user spaceship, alert user to add one.
  if (num_u_ship == 0) displayMessage("alert_info", "You must place an user spaceship!");
  // Check and exit game if reach the end
  else if (checkEnd()) exitGame();
  // Else enter play stage.
  else startGame();
}

/**
 *  Start game
 */
function startGame() {
  stage = "play";
  document.getElementById("startBtn").style.visibility = "hidden";
  document.getElementById("moveUserBtn").style.visibility = "visible";
  document.getElementById("direction").style.visibility = "visible";
  document.getElementById("exitBtn").style.visibility = "visible";
  document.getElementById("direction").focus();
  displayMessage("stage", "Enter [a,d,w,s] to move your ship");
  displayMessage("guide_info", "");
  displayMessage("alert_info", "");
  displayInfo();
}

/**
 *  Exit game
 */
function exitGame() {
  // Set button visibility status and update page
  stage = "end";
  document.getElementById("startBtn").style.visibility = "hidden";
  document.getElementById("moveUserBtn").style.visibility = "hidden";
  document.getElementById("direction").style.visibility = "hidden";
  document.getElementById("exitBtn").style.visibility = "hidden";
  document.getElementById("restartBtn").style.visibility = "visible";

  // Declare the winner
  var result = "";
  if (num_r_ship == 0 && num_u_ship > 0) result += "You Win!";
  else if (num_u_ship == 0 && num_r_ship > 0) result += "Computer Win!";
  else result += "Draw!";
  displayMessage("guide_info", "");
  displayMessage("alert_info", "");
  displayMessage("stage", "Game over! " + result);
  displayInfo();
}

/**
 * Check if the cell is in grid
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function isInGrid(x, y) {
  return x >= 0 && x < grid_col && y >= 0 && y < grid_row;
}

/**
 * Check if ship is trapped
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param type the type of ship
 */
function isTrapped(x, y, type) {
  // Robot ship has 8 options; user ship has 4 options 
  var arr = type == "r" ? surroundings : [[1, 0],[0, 1],[-1, 0],[0, -1]];

  // Determine if there exists valid cells
  var valid_cells = arr.length;
  for (let i = 0; i < arr.length; i++) {
    var nx = x + arr[i][0];
    var ny = y + arr[i][1];

    // Valid cell decrease
    if (!isInGrid(nx, ny) || (isInGrid(nx, ny) && grid[nx][ny] == "a")) valid_cells--;
    // If type is robot ship, alse need to decrease when surrounding is robot ship
    else if (type == "r" && isInGrid(nx, ny) && grid[nx][ny] == "r") valid_cells--;
  }

  return valid_cells == 0 ? true : false;
}

/**
 * Check if ships can move
 */
function isAbleToMove() {
  // Determine if user ship can move
  if (num_u_ship != 0 && !isTrapped(u_ship.row, u_ship.col, "u")) return true;
  
  // Determine if robot ships can move
  for (let i = 0; i < arr_r.length; i++) {
    if (arr_r[i] != undefined && !isTrapped(arr_r[i].row, arr_r[i].col, "r")) return true;
  }

  // All ships can not move
  return false;
}

/**
 * Help function to update single cell
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function updateCell(x, y) {
  var id = x.toString() + y.toString();
  document.getElementById(id).innerHTML = charToImg(grid[x][y]);
}

/**
 * Update user ship's current cell
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function updateUserCurrentCell(x, y) {
  // Set user ship to null
  if (grid[x][y] == "u") {
    grid[x][y] = " ";
    updateCell(x, y);
  }
  // Set hidden mine to active mine
  if (grid[x][y] == "hidden_m") {
    grid[x][y] = "active_m";
    updateCell(x, y);
  }
}

/**
 * Update user ship's next cell.
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function updateUserNextCell(x, y) {
  // Set null to user ship
  if (grid[x][y] == " ") {
    grid[x][y] = "u";
    updateCell(x, y);
  }
  // If hit robot ship, user ship number decrease
  if (grid[x][y] == "r") {
    num_u_ship--;
  }
  // Set mine or active mine to hidden mine
  if (grid[x][y] == "m" || grid[x][y] == "active_m") {
    // If it is mine but not active
    if (grid[x][y] == "m") {
      for (let i = 0; i < arr_m.length; i++) {
        // Set it active
        if (arr_m[i] != undefined && arr_m[i].row == x && arr_m[i].col == y) arr_m[i].isActive = true;
      }
      // Decrease number of mine
      num_i_mine--;
    }
    // Update cell value
    grid[x][y] = "hidden_m";
    updateCell(x, y);
  }
}

/**
 * Update computer ship's current cell.
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function updateCompCurrentCell(x, y) {
  // Set robot ship to null
  if (grid[x][y] == "r") {
    grid[x][y] = " ";
    updateCell(x, y);
  }
}

/**
 * Update computer ship's next cell.
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function updateCompNextCell(x, y) {
  // Set null to robot ship
  if (grid[x][y] == " ") {
    grid[x][y] = "r";
    updateCell(x, y);
  }
  // Set user ship to robot ship and decrease user ship
  if (grid[x][y] == "u") {
    grid[x][y] = "r";
    num_u_ship--;
    updateCell(x, y);
  }
  // Set mine to robot ship
  if (grid[x][y] == "m") {
    grid[x][y] = "r";
    // remove mine by setting it to undefined
    for (let i = 0; i < arr_m.length; i++) {
      if (arr_m[i] != undefined && arr_m[i].row == x && arr_m[i].col == y) {
        arr_m[i] = undefined;
      }
    }
    // Decrease number of mine and update
    num_i_mine--;
    updateCell(x, y);
  }
}

/**
 * Move the user ship
 * @param dir the moving direction of user ship
 */
function moveUserShip(dir) {
  displayMessage("alert_info", "<br />");
  // Move left
  if (dir == "a") {
    // On the border
    if (u_ship.col == 0) {
      displayMessage("alert_info", "Reach the border -- move fails!");
    } 
    // Hit asteroid
    else if (grid[u_ship.row][u_ship.col - 1] == "a") {
      displayMessage("alert_info", "Reach the asteroid -- move fails");
    } 
    // Valid situation
    else {
      updateUserCurrentCell(u_ship.row, u_ship.col);
      u_ship.col--;
      updateUserNextCell(u_ship.row, u_ship.col);
    }
  }
  // Move right
  else if (dir == "d") {
    // On the border
    if (u_ship.col == grid_col - 1) {
      displayMessage("alert_info", "Reach the border -- move fails!");
    } 
    // Hit asteroid
    else if (grid[u_ship.row][u_ship.col + 1] == "a") {
      displayMessage("alert_info", "Reach the asteroid -- move fails");
    } 
    // Valid situation
    else {
      updateUserCurrentCell(u_ship.row, u_ship.col);
      u_ship.col++;
      updateUserNextCell(u_ship.row, u_ship.col);
    }
  }
  // Move up
  else if (dir == "w") {
    // On the border
    if (u_ship.row == 0) {
      displayMessage("alert_info", "Reach the border -- move fails!");
    } 
    // Hit asteroid
    else if (grid[u_ship.row - 1][u_ship.col] == "a") {
      displayMessage("alert_info", "Reach the asteroid -- move fails");
    } 
    // Valid situation
    else {
      updateUserCurrentCell(u_ship.row, u_ship.col);
      u_ship.row--;
      updateUserNextCell(u_ship.row, u_ship.col);
    }
  }
  // Move down
  else if (dir == "s") {
    // On the border
    if (u_ship.row == grid_row - 1) {
      displayMessage("alert_info", "Reach the border -- move fails!");
    } 
    // Hit asteroid
    else if (grid[u_ship.row + 1][u_ship.col] == "a") {
      displayMessage("alert_info", "Reach the asteroid -- move fails");
    } 
    // Valid situation
    else {
      updateUserCurrentCell(u_ship.row, u_ship.col);
      u_ship.row++;
      updateUserNextCell(u_ship.row, u_ship.col);
    }
  }
  // Do destroy
  processDestroy();

  // Clear the input box for user convenience
  document.getElementById("direction").value = "";
  document.getElementById("direction").focus();

  // Check if the game is over
  if (checkEnd()) exitGame();
  
}

/**
 * Move a computer ships
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function moveCompShip(x, y, i) {
  updateCompCurrentCell(x, y);
  updateCompNextCell(arr_r[i].row, arr_r[i].col);
}

/**
 * Move a computer ship when near user ship
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function isNearUserShip(x, y, i) {
  // Do while loop to see if a robot ship can move to a user ship
  var idx = 0;
  while (idx < surroundings.length) {
    // Get new position
    var nx = x + surroundings[idx][0];
    var ny = y + surroundings[idx][1];

    // Check and update robot ship's position
    if (isInGrid(nx, ny) && grid[nx][ny] == "u") {
      arr_r[i].row = nx;
      arr_r[i].col = ny;
      return true;
    }
    idx++;
  }
  // If there is no user ship around, return false
  return false;
}

/**
 * Move a computer ship when near inactive mine
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function isNearMine(x, y, i) {
  // Do while loop to see if a robot ship can move to a mine
  var idx = 0;
  while (idx < surroundings.length) {
    // Get new position
    var nx = x + surroundings[idx][0];
    var ny = y + surroundings[idx][1];

    // Check and update robot ship's position
    if (isInGrid(nx, ny) && grid[nx][ny] == "m") {
      arr_r[i].row = nx;
      arr_r[i].col = ny;
      return true;
    }
    idx++;
  }
  // If there is no user ship around, return false
  return false;
}

/**
 * Get a random option for a computer ship
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function getRandOption(x, y, i) {
  // Declare two array to store options
  var danger_zone = [];
  var safe_zone = [];

  // Do for loop to check every surroundings
  for (let k = 0; k < surroundings.length; k++) {
    // Get new position
    var nx = x + surroundings[k][0];
    var ny = y + surroundings[k][1];

    // Check if new position is in the grid
    if (isInGrid(nx, ny)) {
      // Get background color to check if it is a surrounding of an active mine
      var id = nx.toString() + ny.toString();
      var bg = document.getElementById(id).style.backgroundColor;
      if (bg == "rgb(240, 148, 150)") danger_zone.push(surroundings[k]);

      // Else check if it is a valid option to go
      else if (grid[nx][ny] != "a" && grid[nx][ny] != "r") safe_zone.push(surroundings[k]);
    }
  }

  // There exists safe way to go, then get an option and go
  if (safe_zone.length != 0) {
    var idx = Math.round(Math.random() * (safe_zone.length - 1));
    arr_r[i].row = x + safe_zone[idx][0];
    arr_r[i].col = y + safe_zone[idx][1];
  }
  // No safe way to go, then kill itself
  else if (safe_zone.length == 0 && danger_zone.length != 0) {
    var idx = Math.round(Math.random() * (danger_zone.length - 1));
    arr_r[i].row = x + danger_zone[idx][0];
    arr_r[i].col = y + danger_zone[idx][1];
  }
}

/**
 * Get a moving option by encircle strategy for a computer ship
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function getEncircleOption(x, y, i) {
  // New position
  var dx = x - u_ship.row;
  var dy = y - u_ship.col;

  // In the same row
  if (dx == 0) return dy > 0 ? isDirectValid(x, y - 1, i) : isDirectValid(x, y + 1, i);
  // In the same col
  if (dy == 0) return dx > 0 ? isDirectValid(x - 1, y, i) : isDirectValid(x + 1, y, i);
  
  var option;
  // User ship is up left
  if (dx > 0 && dy > 0) option = [[-1, -1], [-1, 0], [0, -1]];
  // User ship is up right
  else if (dx > 0 && dy < 0) option = [[-1, 1], [-1, 0], [0, 1]];
  // User ship is down left
  else if (dx < 0 && dy > 0) option = [[1, -1], [1, 0], [0, -1]];
  // User ship is down right
  else if (dx < 0 && dy < 0) option = [[1, 1], [1, 0], [0, 1]];

  return isDiaValid(x, y, i, option);
}

/**
 * Check if horizon or vertical option is valid
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function isDirectValid(x, y, i) {
  // To check if new postion is in the grid
  if (isInGrid(x, y)) {
    var id = x.toString() + y.toString();
    var bg = document.getElementById(id).style.backgroundColor;
    // To check if new position is valid
    if (grid[x][y] != "a" && grid[x][y] != "r" && bg != "rgb(240, 148, 150)") {
      arr_r[i].row = x;
      arr_r[i].col = y;
      return true;
    }
  }
  return false;
}

/**
 * Check if dianogal option is valid
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 * @param i the ith computer ship in the array
 */
function isDiaValid(x, y, i, option) {
  // Sample possible option and do check
  while (option.length != 0) {
    console.log("( " + x + "," + y  + ") : Enter isDiaValid()");
    var idx = Math.round(Math.random() * (option.length - 1));
    var nx = x + option[idx][0];
    var ny = y + option[idx][1];
    // To check if new postion is in the grid
    if (isInGrid(nx, ny)) {
      var id = nx.toString() + ny.toString();
      var bg = document.getElementById(id).style.backgroundColor;
      // To check if new position is valid
      if (grid[nx][ny] != "a" && grid[nx][ny] != "r" && bg != "rgb(240, 148, 150)") {
        arr_r[i].row = nx;
        arr_r[i].col = ny;
        return true;
      } 
      // Remove invalid option
      else {
        option.splice(idx);
        continue;
      }
    }
  }
  return false;
}

/**
 * Check if there are asteroids or computer ships around the active mine
 */
function processDestroy() {
  // Render destroy by iterating the array of mines
  for (let i = 0; i < arr_m.length; i++) {
    // For each valid active mine
    if (arr_m[i] != undefined && arr_m[i].isActive == true) {
      // Iterate its surroundings
      for (let j = 0; j < surroundings.length; j++) {
        var x = arr_m[i].row - surroundings[j][0];
        var y = arr_m[i].col - surroundings[j][1];
        
        // If it is in the grid, render the color of the cell
        if (isInGrid(x, y)) {
          var id = x.toString() + y.toString();
          document.getElementById(id).style.backgroundColor = "#f09496";
        }
      }
      // Clear surrondings
      destroySurrondings(arr_m[i].row, arr_m[i].col);
    }
  }
}

/**
 * Destroy the asteroids or computer ships around the active mine.
 * @param x x coordinate of the cell
 * @param y y coordinate of the cell
 */
function destroySurrondings(x, y) {
  // Detroy robot ships by iterating the array of robot ships
  for (let i = 0; i < arr_r.length; i++) {
    if (arr_r[i] != undefined && Math.abs(arr_r[i].row - x) <= 1 && Math.abs(arr_r[i].col - y) <= 1) {
      grid[arr_r[i].row][arr_r[i].col] = " ";
      var id = arr_r[i].row.toString() + arr_r[i].col.toString();
      document.getElementById(id).innerHTML = charToImg(" ");
      arr_r[i] = undefined;
      num_r_ship--;
    }
  }
  // Detroy asteroids iterating the array of asteroids
  for (let i = 0; i < arr_a.length; i++) {
    if (arr_a[i] != undefined && Math.abs(arr_a[i].row - x) <= 1 && Math.abs(arr_a[i].col - y) <= 1) {
      grid[arr_a[i].row][arr_a[i].col] = " ";
      var id = arr_a[i].row.toString() + arr_a[i].col.toString();
      document.getElementById(id).innerHTML = charToImg(" ");
      arr_a[i] = undefined;
    }
  }
}

/**
 * Main function that process computer ship behavior
 */
function compPlay() {
  // Move each robot ship by iterating the array of robot ships
  for (let i = 0; i < arr_r.length; i++) {
    // For each alive robot ship
    if (arr_r[i] != undefined) {
      // Get current options
      var x = arr_r[i].row;
      var y = arr_r[i].col;

      // Try to move to a cell containing the user's ship
      if (isNearUserShip(x, y, i)) moveCompShip(x, y, i);

      // Try to move to a cell containing mine
      else if (isNearMine(x, y, i)) moveCompShip(x, y, i);

      // Move to an arbitrary cell if the r ship isn't trapped
      else if (!isTrapped(x, y, "r") && num_u_ship == 1) {
        if (!getEncircleOption(x, y, i)) { 
          getRandOption(x, y, i); 
        }
        // After get an option, move the robot ship
        moveCompShip(x, y, i);
      }

      // Do destroy
      processDestroy();

      // Check if game is over
      if (checkEnd()) exitGame();
    }
  }
  // Increase game round
  round++;
}

/**
 * Main function that process user behavior
 */
 function userPlay() {
  // User's turn: get valid input as direction
  var dir = document.getElementById("direction").value;
  if (dir == "a" || dir == "d" || dir == "w" || dir == "s") {
    moveUserShip(dir);
    displayInfo();
    compPlay();
    displayInfo();
  }
  // Invalid input
  else {
    displayMessage("alert_info", "Invalid Direction!");
    document.getElementById("direction").value = "";
    document.getElementById("direction").focus();
  }
}

// Initialize the game
initialize();
