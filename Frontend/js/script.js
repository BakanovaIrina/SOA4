const API_URL = "https://127.0.0.1:4501/api/v1/products";
const API_EBAY = "https://localhost:8443/api/v1/ebay/filter"
let productStructure;
let deleteStructure;
let numberStructure;
let tableStructure;

async function sendRequest(method, endpoint = "", body = null, params = {}, api=API_URL) {
  const queryParams = new URLSearchParams(params).toString();
  const url = queryParams ? `${api}${endpoint}?${queryParams}` : `${api}${endpoint}`;

  const options = {
    method: method,
    headers: {
      "Content-Type": "application/json",
      "Accept": "*/*"
    }
  };


  if (body) options.body = JSON.stringify(body);

  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      displayStatusCode(response.status);
      productStructure = false;
      deleteStructure = false;
      numberStructure = false;
      tableStructure = false;
      return;
    }

    displayStatusCode(response.status);

    const dataText = await response.text();

    displayProductInfo(dataText);
  } catch (error) {
    alert("Ошибка запроса: " + error.message);
    console.error("Ошибка сети:", error);
  }
}

async function sendRequestEbay(method, endpoint = "", body = null, params = {}, api=API_URL) {
  const queryParams = new URLSearchParams(params).toString();
  const url = queryParams ? `${api}${endpoint}?${queryParams}` : `${api}${endpoint}`;

  const options = {
    method: method,
    headers: {
      "Content-Type": "application/json",
      "Accept": "*/*"
    }
  };


  if (body) options.body = JSON.stringify(body);

  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      displayStatusCode(response.status);
      productStructure = false;
      deleteStructure = false;
      numberStructure = false;
      tableStructure = false;
      return;
    }

    displayStatusCode(response.status);

    const dataText = await response.text();

    displayProductInfo(dataText, true);
  } catch (error) {
    alert("Ошибка запроса: " + error.message);
    console.error("Ошибка сети:", error);
  }
}

function displayStatusCode(statusCode){
  const statusDiv = document.getElementById('status-code');
  statusDiv.innerHTML = `
  <p><strong>Code: </strong>${statusCode}<\p>
  <p></p>`;
}

function displayProductInfo(dataText, isEbay) {
  if(numberStructure){
    const productInfoDiv = document.getElementById('product-info');
    const resultTable = document.getElementById('result-table');
    productInfoDiv.innerHTML = `<p><strong>Result: </strong> ${dataText}</p>`;

    resultTable.innerHTML = "";
  }
  if(deleteStructure){
    const productInfoDiv = document.getElementById('product-info');
    const resultTable = document.getElementById('result-table');
    productInfoDiv.innerHTML = ``;
    resultTable.innerHTML = "";
  }
  if(productStructure){
    const productInfoDiv = document.getElementById('product-info');
    const resultTable = document.getElementById('result-table');
    product = JSON.parse(dataText);

    productInfoDiv.innerHTML = `
        <p><strong>ID:</strong> ${product.id}</p>
        <p><strong>Name:</strong> ${product.name}</p>
        <p><strong>Coordinates:</strong> x: ${product.coordinates.x}, y: ${product.coordinates.y}</p>
        <p><strong>Creation Date:</strong> ${new Date(product.creationDate).toLocaleString()}</p>
        <p><strong>Price:</strong> $${product.price}</p>
        <p><strong>Manufacture Cost:</strong> $${product.manufactureCost}</p>
        <p><strong>Unit of Measure:</strong> ${product.unitOfMeasure}</p>
        <p><strong>Owner ID:</strong> ${product.owner.id}</p>
        <p><strong>Owner Name:</strong> ${product.owner.name}</p>
        <p><strong>Owner Birthday:</strong> ${new Date(product.owner.birthday).toLocaleString()}</p>
        <p><strong>Owner Height:</strong> ${product.owner.height} cm</p>
        <p><strong>Owner Weight:</strong> ${product.owner.weight} kg</p>
        <p><strong>Owner Location:</strong> x: ${product.owner.location.x}, y: ${product.owner.location.y}, Name: ${product.owner.location.name}</p>
    `;
    resultTable.innerHTML = "";
  }
  if(tableStructure){
    if (tableStructure) {
      const productInfoDiv = document.getElementById('product-info');
      const resultTable = document.getElementById('result-table');

      resultTable.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Coordinates</th>
                <th>Creation Date</th>
                <th>Price</th>
                <th>Manufacture Cost</th>
                <th>Unit of Measure</th>
                <th>Owner</th>
                <th>Owner Location</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;

      const tbody = resultTable.querySelector("tbody");

      const parsedData = JSON.parse(dataText);
      let products;

      if(isEbay){
        products = parsedData.products;
      }
      else {
        products = parsedData;
      }

      products.forEach(product => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>x: ${product.coordinates.x}, y: ${product.coordinates.y}</td>
            <td>${new Date(product.creationDate).toLocaleString()}</td>
            <td>$${product.price}</td>
            <td>$${product.manufactureCost}</td>
            <td>${product.unitOfMeasure}</td>
            <td>${product.owner.name} (ID: ${product.owner.id})</td>
            <td>x: ${product.owner.location.x}, y: ${product.owner.location.y}, Name: ${product.owner.location.name}</td>
        `;
        tbody.appendChild(row);
      });

      productInfoDiv.innerHTML = "";
    }
  }
  productStructure = false;
  deleteStructure = false;
  numberStructure = false;
  tableStructure = false;
}

async function getProducts() {
  tableStructure = true;

  const sort = document.querySelector('[name="sort"]').value.trim();
  const filter = document.querySelector('[name="filter"]').value.trim();
  const page = document.querySelector('[name="page"]').value || "1";
  const size = document.querySelector('[name="size"]').value || "10";

  let queryParams = [];

  if (sort) queryParams.push(`sort=${encodeURIComponent(sort)}`);
  if (filter) queryParams.push(`filter=${encodeURIComponent(filter)}`);
  if (page) queryParams.push(`page=${page}`);
  if (size) queryParams.push(`size=${size}`);

  const queryString = queryParams.length ? `?${queryParams.join("&")}` : "";

  await sendRequest("GET", queryString);
}

async function addProduct() {
  productStructure = true;
  const productJson = document.getElementById("productJson").value;
  let product;
  try {
    product = JSON.parse(productJson);
  } catch (error) {
    alert('Error parsing JSON: ' + error.message);
  }
  await sendRequest("POST", "", product);
}

async function getProductById() {
  productStructure = true;
  const productId = document.querySelector('[name="id"]').value;
  if (!productId) {
    alert('Please enter a valid ID');
    return;
  }
  await sendRequest("GET", `/${productId}`);
}

async function updateProduct() {
  productStructure = true;
  const productJson = document.getElementById("updatedProduct").value;
  let updatedProduct;
  try {
    updatedProduct = JSON.parse(productJson);
  } catch (error) {
    alert('Error parsing JSON: ' + error.message);
  }

  const productId = document.querySelector('[name="idUpdate"]').value;

  if (!productId) {
    alert('Please enter a valid ID');
    return;
  }
  await sendRequest("PUT", `/${productId}`, updatedProduct);
}

async function deleteProduct() {
  deleteStructure = true;
  const productId = document.querySelector('[name="idDelete"]').value;
  if (!productId) {
    alert('Please enter a valid ID');
    return;
  }
  await sendRequest("DELETE", `/${productId}`);
}

async function deleteProductsByUnit() {
  deleteStructure = true;
  const unit = document.getElementById("unitOfMeasureInput").value;

  await sendRequest("DELETE", `/unitOfMeasure/${unit}`);
}

async function getProductWithMinPrice() {
  productStructure = true;
  await sendRequest("GET", "/min-price");
}

async function getCountByManufactureCost() {
  numberStructure = true;
  const costManufacture = document.querySelector('[name="costManufacture"]').value;
  if (!costManufacture) {
    alert('Please enter a valid cost');
    return;
  }
  await sendRequest("GET", `/manufactureCost/${costManufacture}`);
}

async function getByManufacturer(){
  tableStructure = true;
  const id = document.getElementById("ebay-id").value;
  const size = document.getElementById("size-ebay").value;
  const page = document.getElementById("page-ebay").value;

  if (!id) {
    alert("Please input id");
    return;
  }
  const params = {
    page: page,
    size: size
  };

  await sendRequestEbay("POST", `/manufacturer/${id}`, null, params, API_EBAY);
}

async function getListWithCurrentUnit(){
  tableStructure = true;
  const unit = document.getElementById("unitOfMeasureInput1").value;
  const size = document.getElementById("size-ebay1").value;
  const page = document.getElementById("page-ebay1").value;

  if(!unit){
    alert("Please input unit");
    return;
  }

  const params = {
    page: page,
    size: size
  };

  await sendRequestEbay("POST", `/unit-of-measure/${unit}`, null, params, API_EBAY);
}

