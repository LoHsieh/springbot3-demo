// 產品類型定義
export interface Product {
    id: number
    name: string
    description: string
    price: number
    stock: number
    categoryId?: number
    sellerId?: number
    imageUrl?: string
    createdAt?: string
    updatedAt?: string
}

// 訂單項目類型
export interface OrderItem {
    id: number
    productId: number
    quantity: number
    price: number
    product?: Product
}

// 訂單類型
export interface Order {
    id: number
    userId: number
    totalAmount: number
    status: string
    createdAt: string
    items?: OrderItem[]
}
