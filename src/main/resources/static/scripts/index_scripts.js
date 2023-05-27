const formulary = document.querySelector("form")
const button = document.querySelector("button")
const nameInput = document.querySelector(".name")
const barCodeInput = document.querySelector(".barCode")
const quantityMinInput = document.querySelector(".quantityMin")
const initialBalanceInput = document.querySelector(".initialBalance")
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
    const productCategory = productCategorySelect.value;

    fetch("http://localhost:8081/server/products/save",
        {
            headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({
                name: nameInput.value,
                barCode: barCodeInput.value,
                quantityMin: quantityMinInput.value,
                initialBalance: initialBalanceInput.value,
                inventory: {
                productCategory: productCategorySelect.value
                }
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
            // Realize as ações necessárias após o sucesso do salvamento
            })
            .catch(function (error) {
                console.log(error);
                showMessage("Erro ao salvar o produto.");
                // Realize as ações necessárias em caso de erro
            });
}

function clean(){
        nameInput.value = ""
        barCodeInput.value = ""
        quantityMinInput.value = ""
        initialBalanceInput.value = ""
        productCategorySelect.value = ""
}

formulary.addEventListener("submit", function (event){
        event.preventDefault();
        register();
        clean();
});