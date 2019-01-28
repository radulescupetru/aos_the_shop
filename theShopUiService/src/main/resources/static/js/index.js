$(document).ready(function(){
cart = []
$(".add-to-cart-button").on("click",function(){
	card = $(this).parent();
	product_name = card.find(".product-name").html()
	product_price = card.find(".product-price").html()
	console.log(product_price);
	console.log(product_name);
	product = {};
	product["product_name"]=product_name;
	product["product_price"]=product_price;
	cart.push(product);
	});
$(".cart").on("click",function(){
	modal_body = $(".modal-body");
	$(".modal-body").empty();
	for (var i = cart.length - 1; i >= 0; i--) {
		modal_body.append(
			 $('<div/>', {'class':'row modal-body-row'}).append(
			    $('<div/>', {'class': 'col-6'}).append(
			        $('<p/>', {'class': 'product-name',text:cart[i]['product_name']})
			    ),
			    $('<div/>', {'class': 'col-6'}).append(
			        $('<p/>', {'class': 'product-price',text:cart[i]['product_price']})
			        )
		    )
		)
	}
});
});