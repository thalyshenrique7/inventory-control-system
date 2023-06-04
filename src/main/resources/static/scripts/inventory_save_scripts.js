const formulary = document.querySelector("form")
const button = document.querySelector("button")
const nameInput = document.querySelector(".name")
const barCodeInput = document.querySelector(".barCode")
const quantityMinInput = document.querySelector(".quantityMin")
const balanceInput = document.querySelector(".balance")
const priceInput = document.querySelector(".price")
const productCategorySelect = document.querySelector(".productCategory");
const movementTypeSelect = document.querySelector(".movementType");
const reasonInput = document.querySelector(".reason");
const documentInput = document.querySelector(".document");



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
    fetch("http://localhost:8081/server/inventory/save",
        {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({
                reason: reasonInput.value,
                document: documentInput.value,
                product: {
                    name: nameInput.value,
                    barCode: barCodeInput.value,
                    quantityMin: quantityMinInput.value,
                    balance: balanceInput.value,
                    price: priceInput.value
                },
                movementType: movementTypeSelect.value,
                productCategory: productCategorySelect.value,
            })

        })  .then(function (response) {
        if (response.status === 201) {
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
    barCodeInput.value = ""
    quantityMinInput.value = ""
    balanceInput.value = ""
    priceInput.value = ""
    productCategorySelect.value = ""
    movementTypeSelect.value = ""
    reasonInput.value = ""
    documentInput.value = ""
}

formulary.addEventListener("submit", function (event){
    event.preventDefault();
    register();
    clean();
});