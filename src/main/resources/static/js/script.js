console.log("Script Load");


// Change theme work
let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
})

function changeTheme(){
    // Set to web Page
    
    changePageTheme(currentTheme, currentTheme);

    // Set the listener to change theme
    const changeThemeButton = document.querySelector("#theme_change_button");

    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        console.log("Button Clicked");
        if(currentTheme == "dark"){
            // Theme to light
            currentTheme = "light";
        }
        else{
            // Theme to dark
            currentTheme = "dark";
        }
        changePageTheme(currentTheme, oldTheme);
    })
}

// Set theme to local storage

function setTheme(theme){
    localStorage.setItem("theme", theme);
}

// Get theme from local storage

function getTheme(){
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

// Change Page Theme
function changePageTheme(theme, oldTheme){
    
    // Update in local storage
    setTheme(currentTheme);

    // Remove the current theme
    document.querySelector("html").classList.remove(oldTheme);

    // Set the current theme
    document.querySelector("html").classList.add(currentTheme);

    // Change the text of button
    document.querySelector("#theme_change_button").querySelector("span").textContent = theme == "dark" ? "Light" : "Dark";
}

function setHidden(){
  const menu = document.getElementById("navbar-cta");
  const toggleButton = document.getElementById("menu-toggle");

  if (!menu || !toggleButton) return;

  // Function: close menu on scroll
  function closeMenuOnScroll() {
    if (!menu.classList.contains("hidden")) {
      console.log("Closing menu on scroll...");
      menu.classList.add("hidden");
      toggleButton.setAttribute("aria-expanded", "false");
    }
  }

  // Listen for scroll
  window.addEventListener("scroll", closeMenuOnScroll);

  // Keep aria-expanded in sync when toggling
  toggleButton.addEventListener("click", () => {
    const isExpanded = toggleButton.getAttribute("aria-expanded") === "true";
    toggleButton.setAttribute("aria-expanded", String(!isExpanded));
    menu.classList.toggle("hidden");
  });
}

setHidden();

// End of change page theme work