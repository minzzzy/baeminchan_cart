document.addEventListener('DOMContentLoaded', () => {
    new Products();
});

class Products {
    constructor() {
        this.unit = ' 개';
        this.registerEvent();
    }

    registerEvent() {
        this.addProductInCart();
    }

    changeCartNumber(cartNum) {
        if (cartNum !== null) {
            $('p.top_box_number').textContent = cartNum + this.unit;
        }
    }

    showSelectedProduct(target) {
        let prdDescription = target.closest('dl').querySelector('.prd_tlt > a > span').textContent;
        let basketToaster = $('#basket-toaster');
        basketToaster.childNodes.forEach(element => {
            if (element.className === 'prd_name') {
                element.innerHTML = prdDescription;
            }
        });
        basketToaster.style.display = 'block';
        setTimeout(() => {
            basketToaster.style.display = 'none';
        }, 2000);
    }

    addProductInCart() {
        $('#products').addEventListener('click', (e) => {
            let target = e.target;
            let productId = target.getAttribute('data-id');
            if (target.tagName === 'BUTTON') {
                this.showSelectedProduct(target);
                fetchManager({
                    url:'/api/cart/products/' + productId,
                    method: "POST",
                    onSuccess: ({json}) => {
                        this.changeCartNumber(json.data);
                    },
                    onFailed: () => {
                        alert('잘못된 요청입니다.');
                    },
                    onError: () => {
                        alert('요청중 문제가 발생하였습니다. 재접속 후 시도해주세요.');
                    }
                });
            }
        })
    }
}