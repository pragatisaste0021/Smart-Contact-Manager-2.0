console.log("Contatcs Js");

const baseURL = "http://myweb10.ap-south-1.elasticbeanstalk.com";

// const baseURL = "http://localhost:8081";

const viewContactModal = document.getElementById("view_contact_modal");


// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
    try{
        console.log(id);
        const data = await (await fetch(`${baseURL}/api/contacts/${id}`)).json(); 
        console.log(data);
        console.log(data.name);
        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.email;
        document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
        document.querySelector("#contact_address").innerHTML = data.address;

        const about = document.querySelector("#contact_about");
        if(data.description){
            about.innerHTML = data.description;
        }
        else{
            about.innerHTML = "I am a Programmer";
        }

        const favoriteContact = document.querySelector("#contact_favorite");
        if(data.favorite){
            favoriteContact.innerHTML = '<i class="fas fa-star text-yellow-400"></i> <i class="fas fa-star text-yellow-400"></i> <i class="fas fa-star text-yellow-400"></i> <i class="fas fa-star text-yellow-400"></i> <i class="fas fa-star text-yellow-400"></i>';
        }
        else{
            favoriteContact.innerHTML = "Not Favorite Contact";
        }

        document.querySelector("#contact_image").src  = data.picture;
        document.querySelector("#contact_website").innerHTML = data.websiteLink;
        document.querySelector("#contact_website").href = data.websiteLink;
        document.querySelector("#contact_linkedin").innerHTML = data.linkedInLink;
        document.querySelector("#contact_linkedin").href = data.linkedInLink;
        openContactModal();
    }
    catch(error){
        console.log(error);
    }
}

// Delete Contact

function deleteContact(id){
    Swal.fire({
        icon : "warning",
        title: "Do you want to delete the contact?",
        showCancelButton: true,
        confirmButtonText: "Delete",
        customClass: {
            confirmButton: "my-confirm-button",
            cancelButton: "my-cancel-button"
        }
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contacts/delete/` + id;
            window.location.replace(url);
          Swal.fire({
            text: "Saved!",
            icon: "success",
            customClass: {
                confirmButton: "my-confirm-button"
            }
          });
        } 
      });
}