import { LitElement, html } from '';

class VehicleItem extends LitElement {
  static get properties() {
    return {
      vehicleData: { type: Object },
      formattedPrice: { type: String },
    };
  }

  render() {
    return html`
      <vaadin-card>
        <div class="image-container">
          <img src="${this.vehicleData.vehicleImg}" alt="${this.vehicleData.name}">
        </div>
        <div class="details-container">
          <h2>${this.vehicleData.name}</h2>
          <p class="rating">${this.vehicleData.rating} <vaadin-icon icon="vaadin:star"></vaadin-icon></p>
          <p class="price">Price: &#8377;${this.formattedPrice}</p>
        </div>
      </vaadin-card>
    `;
  }

  firstUpdated() {
    this.formattedPrice = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(this.vehicleData.price);
  }
}