"use strict";
console.log(1 + 5)
const url = "localhost:8080/admin"
let response = await fetch(url);

if(response.ok) {
    let json = await response.json()

    console.log('Data'+ json.data())
} else {
    alert("Ошибка HTTP: " + response.status);
}