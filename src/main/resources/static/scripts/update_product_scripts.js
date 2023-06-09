const formulary = document.querySelector("form")
const button = document.querySelector("button")
const nameInput = document.querySelector(".name")
const quantityMinInput = document.querySelector(".quantityMin")
const priceInput = document.querySelector(".price")
const productCategorySelect = document.querySelector(".productCategory");

function showMessage(message) {
    var messageBox = document.createElement("div");
    messageBox.classList.add("messageBox");
    var messageText = document.createElement("p");
    messageText.textContent = message;
    messageBox.appendChild(messageText);
    document.body.appendChild(messageBox);

    setTimeout(function () {
        document.body.removeChild(messageBox);
    }, 3000);
}

function register(){
    const formulary = document.querySelector("form");
    const id = formulary.getAttribute("data-id");

    fetch(`http://localhost:8081/server/products/update/${id}`,
        {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            method: "PUT",
            body: JSON.stringify({
                name: nameInput.value,
                quantityMin: quantityMinInput.value,
                priceUnit: priceInput.value,
                productCategory: productCategorySelect.value
            })

        })  .then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Erro ao salvar o produto.");
        }
    })
        .then(function (data) {
            console.log(data);
            showMessage("Produto cadastrado com sucesso!");
        })
        .catch(function (error) {
            console.log(error);
            showMessage("Erro ao salvar o produto.");
        });
}

function clean(){
    nameInput.value = ""
    quantityMinInput.value = ""
    priceInput.value = ""
    productCategorySelect.value = ""
}

formulary.addEventListener("submit", function (event){
    event.preventDefault();
    register();
    clean();
});