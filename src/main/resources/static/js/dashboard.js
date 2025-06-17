let currentOrderForEdit = null;

document.addEventListener('DOMContentLoaded', () => {
    // Jika ada elemen 'productsContainer' di halaman, muat produknya.
    if (document.getElementById('productsContainer')) {
        loadProducts();
    }
});

async function fetchProducts() {
    const response = await fetch('/api/products');
    if (!response.ok) {
        throw new Error("Gagal memuat data produk dari server.");
    }
    return await response.json();
}

function renderProducts(products) {
    const container = document.getElementById('productsContainer');
    if (!container) return;

    container.innerHTML = '';
    if (!products || products.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-muted p-4">Belum ada produk yang tersedia.</div>';
        return;
    }

    products.forEach(product => {
        const col = document.createElement('div');
        col.className = 'col-lg-4 col-md-6 col-sm-12 mb-4';

        const imageUrl = product.imageUrl || `https://placehold.co/600x400/EEE/31343C?text=No+Image`;
        const activeStatus = product.active ? 'Aktif' : 'Nonaktif';
        const activeClass = product.active ? 'bg-success' : 'bg-secondary';

        // Menggunakan template literals (`) untuk kode yang lebih bersih
        col.innerHTML = `
            <div class="card product-card shadow-sm h-100">
                <img src="<span class="math-inline">\{imageUrl\}" class\="card\-img\-top" alt\="</span>{product.name}" onerror="this.onerror=null;this.src='https://placehold.co/600x400/EEE/31343C?text=No+Image';">
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title mb-1"><span class="math-inline">\{product\.name\}</h5\>
<div class\="mb\-2"\>
<span class\="badge bg\-primary"\></span>{product.type}</span>
                        <span class="badge <span class="math-inline">\{activeClass\}"\></span>{activeStatus}</span>
                    </div>
                    <p class="card-text small text-muted flex-grow-1">${product.description || 'Tidak ada deskripsi.'}</p>
                    <div class="mt-auto">
                        <p class="h5 text-success fw-bold mb-3">Rp <span class="math-inline">\{product\.price\.toLocaleString\('id\-ID'\)\}</p\>
<div class\="d\-flex gap\-2"\>
<button class\="btn btn\-sm btn\-warning flex\-fill" onclick\="editProduct\(</span>{product.id})"><i class="bi bi-pencil-square"></i> Edit</button>
                            <button class="btn btn-sm btn-danger flex-fill" onclick="deleteProduct(${product.id})"><i class="bi bi-trash"></i> Hapus</button>
                        </div>
                    </div>
                </div>
            </div>`;
        container.appendChild(col);
    });
}

async function loadProducts() {
    const container = document.getElementById('productsContainer');
    if (!container) return;

    try {
        const products = await fetchProducts();
        renderProducts(products);
    } catch (error) {
        console.error(error);
        container.innerHTML = `<div class="col-12 text-center text-danger p-4"><strong>Error:</strong> ${error.message}</div>`;
    }
}

window.showAddModal = function() {
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
    document.getElementById('productActive').checked = true;
    document.getElementById('modalTitle').innerText = 'Tambah Produk Baru';
    new bootstrap.Modal(document.getElementById('productModal')).show();
};

window.editProduct = async function(id) {
    try {
        const res = await fetch(`/api/products/${id}`);
        if (!res.ok) throw new Error('Produk tidak ditemukan.');

        const product = await res.json();
        document.getElementById('productId').value = product.id;
        document.getElementById('productName').value = product.name;
        document.getElementById('productType').value = product.type;
        document.getElementById('productCategory').value = product.category;
        document.getElementById('productPrice').value = product.price;
        document.getElementById('productDescription').value = product.description || '';
        document.getElementById('productActive').checked = product.active;
        document.getElementById('productImageUrl').value = product.imageUrl || '';
        document.getElementById('modalTitle').innerText = 'Edit Produk';
        new bootstrap.Modal(document.getElementById('productModal')).show();
    } catch (error) {
        alert(error.message);
    }
};

window.saveProduct = async function() {
    const id = document.getElementById('productId').value;
    const productData = {
        name: document.getElementById('productName').value,
        type: document.getElementById('productType').value,
        category: document.getElementById('productCategory').value,
        price: parseFloat(document.getElementById('productPrice').value),
        description: document.getElementById('productDescription').value,
        active: document.getElementById('productActive').checked,
        imageUrl: document.getElementById('productImageUrl').value
    };

    const url = id ? `/api/products/${id}` : '/api/products';
    const method = id ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(productData)
        });

        if (res.ok) {
            bootstrap.Modal.getInstance(document.getElementById('productModal')).hide();
            alert('Produk berhasil disimpan!');
            loadProducts();
        } else {
            const errorText = await res.text();
            throw new Error(`Gagal menyimpan produk: ${errorText}`);
        }
    } catch (error) {
        alert(error.message);
    }
};

window.deleteProduct = async function(id) {
    if (!confirm('Yakin ingin menghapus produk ini?')) return;
    try {
        const res = await fetch(`/api/products/${id}`, { method: 'DELETE' });
        if (res.ok) {
            alert('Produk berhasil dihapus!');
            loadProducts();
        } else {
            throw new Error('Gagal menghapus produk.');
        }
    } catch (error) {
        alert(error.message);
    }
};

window.editOrder = async function(orderId) {
    try {
        const response = await fetch(`/api/orders/${orderId}`);
        if (!response.ok) throw new Error('Pesanan tidak ditemukan.');

        const order = await response.json();
        
        currentOrderForEdit = order;

        document.getElementById('orderId').value = order.id;
        document.getElementById('orderNumber').innerText = order.id;
        
        // Format tanggal agar hanya menampilkan YYYY-MM-DD
        const orderDate = order.orderDate ? order.orderDate.split('T')[0] : 'Tanggal tidak tersedia';
        document.getElementById('orderDate').innerText = new Date(orderDate).toLocaleDateString('id-ID', { year: 'numeric', month: 'long', day: 'numeric' });

        document.getElementById('customerName').innerText = order.customerName;
        document.getElementById('service').innerText = order.service;
        document.getElementById('kilogram').innerText = `${order.kilogram} kg`;
        document.getElementById('totalPrice').innerText = `Rp ${order.totalPrice.toLocaleString('id-ID')}`;
        
        document.getElementById('orderStatus').value = order.status;

        new bootstrap.Modal(document.getElementById('orderDetailModal')).show();

    } catch (error) {
        console.error('Error:', error);
        alert('Terjadi kesalahan saat mengambil detail pesanan.');
    }
};

window.saveOrder = async function() {
    
    if (!currentOrderForEdit) {
        alert('Data pesanan tidak ditemukan. Coba buka kembali modal.');
        return;
    }
    
    const updatedOrder = { ...currentOrderForEdit }; // Salin objek asli
    updatedOrder.status = document.getElementById('orderStatus').value; // Perbarui statusnya

    const orderId = updatedOrder.id;

    try {
        const response = await fetch(`/api/orders/${orderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedOrder) 
        });

        if (response.ok) {
            alert('Status pesanan berhasil diperbarui!');
            const modal = bootstrap.Modal.getInstance(document.getElementById('orderDetailModal'));
            if (modal) modal.hide();
            location.reload(); 
        } else {
            const errorText = await response.text();
            throw new Error(`Gagal menyimpan perubahan: ${errorText}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
};