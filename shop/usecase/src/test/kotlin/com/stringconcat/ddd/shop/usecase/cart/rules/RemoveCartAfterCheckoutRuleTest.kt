package com.stringconcat.ddd.shop.usecase.cart.rules

import com.stringconcat.ddd.shop.domain.cart
import com.stringconcat.ddd.shop.domain.customerId
import com.stringconcat.ddd.shop.domain.order.ShopOrderCreatedDomainEvent
import com.stringconcat.ddd.shop.domain.orderId
import com.stringconcat.ddd.shop.usecase.TestCartExtractor
import com.stringconcat.ddd.shop.usecase.MockCartRemover
import org.junit.jupiter.api.Test

internal class RemoveCartAfterCheckoutRuleTest {

    @Test
    fun `successfully removed`() {

        val cartRemover = MockCartRemover()
        val cart = cart()
        val cartExtractor = TestCartExtractor().apply {
            this[cart.forCustomer] = cart
        }

        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val event = ShopOrderCreatedDomainEvent(orderId(), cart.forCustomer)

        rule.handle(event)
        cartRemover.verifyInvoked(cart.id)
    }

    @Test
    fun `cart not found`() {

        val cartRemover = MockCartRemover()
        val cartExtractor = TestCartExtractor()
        val rule = RemoveCartAfterCheckoutRule(cartExtractor, cartRemover)
        val event = ShopOrderCreatedDomainEvent(orderId(), customerId())

        rule.handle(event)
        cartRemover.verifyEmpty()
    }
}