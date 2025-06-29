<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>User Management - PhoneHub Admin</title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

                <!-- Cloudinary Upload Widget -->
                <script src="https://upload-widget.cloudinary.com/global/all.js" type="text/javascript"></script>

                <!-- Include Cloudinary Configuration -->
                <%@ include file="../includes/cloudinary-config.jsp" %>
            </head>

            <body style="margin: 0; background: var(--background-color);">
                <!-- Admin Sidebar -->
                <aside class="admin-sidebar">
                    <div style="padding: 2rem 1rem; border-bottom: 1px solid var(--border-color);">
                        <h2 style="color: var(--primary-color); margin: 0;">
                            <i class="fas fa-mobile-alt"></i>
                            PhoneHub Admin
                        </h2>
                    </div>

                    <nav>
                        <ul class="admin-nav">
                            <li>
                                <a href="${pageContext.request.contextPath}/admin">
                                    <i class="fas fa-tachometer-alt"></i>
                                    Dashboard
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/products">
                                    <i class="fas fa-mobile-alt"></i>
                                    Products
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/orders">
                                    <i class="fas fa-shopping-cart"></i>
                                    Orders
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/admin/users" class="active">
                                    <i class="fas fa-users"></i>
                                    Users
                                </a>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <!-- Main Content -->
                <main class="admin-main">
                    <header class="admin-header">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <h1 style="margin: 0; color: var(--text-primary);">User Management</h1>
                                <p style="margin: 0.5rem 0 0 0; color: var(--text-secondary);">
                                    Manage system users and their permissions
                                </p>
                            </div>
                            <button class="btn btn-primary" onclick="showUserForm()">
                                <i class="fas fa-plus"></i>
                                Add New User
                            </button>
                        </div>
                    </header>

                    <!-- Filters and Search -->
                    <section class="admin-filters">
                        <div
                            style="display: grid; grid-template-columns: 1fr auto auto auto; gap: 1rem; align-items: end;">
                            <div>
                                <label for="searchUsers"
                                    style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Search
                                    Users</label>
                                <input type="text" id="searchUsers" placeholder="Search by name, email, or username..."
                                    style="width: 100%; padding: 0.75rem; border: 1px solid var(--border-color); border-radius: 0.5rem;">
                            </div>
                            <div>
                                <label for="roleFilter"
                                    style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Role</label>
                                <select id="roleFilter"
                                    style="padding: 0.75rem; border: 1px solid var(--border-color); border-radius: 0.5rem;">
                                    <option value="">All Roles</option>
                                    <option value="CUSTOMER">Customer</option>
                                    <option value="SALES">Sales</option>
                                    <option value="MANAGER">Manager</option>
                                    <option value="ADMIN">Admin</option>
                                </select>
                            </div>
                            <div>
                                <label for="statusFilter"
                                    style="display: block; margin-bottom: 0.5rem; font-weight: 500;">Status</label>
                                <select id="statusFilter"
                                    style="padding: 0.75rem; border: 1px solid var(--border-color); border-radius: 0.5rem;">
                                    <option value="">All Status</option>
                                    <option value="active">Active</option>
                                    <option value="inactive">Inactive</option>
                                </select>
                            </div>
                            <button type="button" class="btn btn-secondary" onclick="resetFilters()">
                                <i class="fas fa-refresh"></i>
                                Reset
                            </button>
                        </div>
                    </section>

                    <!-- Users Table -->
                    <section class="admin-content">
                        <div class="data-table-container">
                            <table class="data-table" id="usersTable">
                                <thead>
                                    <tr>
                                        <th>User</th>
                                        <th>Contact</th>
                                        <th>Role</th>
                                        <th>Auth Method</th>
                                        <th>Status</th>
                                        <th>Join Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty users}">
                                            <c:forEach var="user" items="${users}">
                                                <tr data-user-id="${user.id}">
                                                    <td>
                                                        <div style="display: flex; align-items: center; gap: 0.75rem;">
                                                            <div class="user-avatar">
                                                                <c:choose>
                                                                    <c:when test="${not empty user.profileImageUrl}">
                                                                        <img src="${user.profileImageUrl}"
                                                                            alt="${user.fullName}"
                                                                            style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover;">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <div
                                                                            style="width: 40px; height: 40px; border-radius: 50%; background: var(--primary-color); 
                                                                        display: flex; align-items: center; justify-content: center; color: white; font-weight: 600;">
                                                                            ${user.fullName.substring(0,1).toUpperCase()}
                                                                        </div>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                            <div>
                                                                <div
                                                                    style="font-weight: 600; color: var(--text-primary);">
                                                                    ${user.fullName}</div>
                                                                <div
                                                                    style="font-size: 0.875rem; color: var(--text-secondary);">
                                                                    @${user.username}</div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div>
                                                            <div style="color: var(--text-primary);">${user.email}</div>
                                                            <c:if test="${not empty user.phone}">
                                                                <div
                                                                    style="font-size: 0.875rem; color: var(--text-secondary);">
                                                                    ${user.phone}</div>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <span class="role-badge role-${user.role.toLowerCase()}">
                                                            <c:choose>
                                                                <c:when test="${user.role == 'CUSTOMER'}"><i
                                                                        class="fas fa-user"></i></c:when>
                                                                <c:when test="${user.role == 'SALES'}"><i
                                                                        class="fas fa-handshake"></i></c:when>
                                                                <c:when test="${user.role == 'MANAGER'}"><i
                                                                        class="fas fa-user-tie"></i></c:when>
                                                                <c:when test="${user.role == 'ADMIN'}"><i
                                                                        class="fas fa-crown"></i></c:when>
                                                            </c:choose>
                                                            ${user.role}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span
                                                            class="auth-badge auth-${user.authProvider.toLowerCase()}">
                                                            <c:choose>
                                                                <c:when test="${user.authProvider == 'LOCAL'}"><i
                                                                        class="fas fa-key"></i></c:when>
                                                                <c:when test="${user.authProvider == 'GOOGLE'}"><i
                                                                        class="fab fa-google"></i></c:when>
                                                            </c:choose>
                                                            ${user.authProvider}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span
                                                            class="status-indicator ${user.active ? 'active' : 'inactive'}">
                                                            <i class="fas fa-circle"></i>
                                                            ${user.active ? 'Active' : 'Inactive'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${user.createdAt}"
                                                            pattern="MMM dd, yyyy" />
                                                    </td>
                                                    <td>
                                                        <div class="action-buttons">
                                                            <button class="btn-icon" onclick="editUser('${user.id}')"
                                                                title="Edit User">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button class="btn-icon"
                                                                onclick="toggleUserStatus('${user.id}', '${user.active}')"
                                                                title="${user.active ? 'Deactivate' : 'Activate'} User">
                                                                <i class="fas fa-${user.active ? 'ban' : 'check'}"></i>
                                                            </button>
                                                            <c:if test="${user.role != 'ADMIN'}">
                                                                <button class="btn-icon btn-danger"
                                                                    onclick="deleteUser('${user.id}')"
                                                                    title="Delete User">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="7"
                                                    style="text-align: center; padding: 3rem; color: var(--text-secondary);">
                                                    <i class="fas fa-users"
                                                        style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.3;"></i>
                                                    <div>No users found</div>
                                                    <button class="btn btn-primary" onclick="showUserForm()"
                                                        style="margin-top: 1rem;">
                                                        Add First User
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination -->
                        <div class="pagination">
                            <button class="btn btn-secondary" onclick="previousPage()" id="prevButton" disabled>
                                <i class="fas fa-chevron-left"></i>
                                Previous
                            </button>
                            <span class="pagination-info">
                                Page <span id="currentPage">${currentPage}</span> of <span id="totalPages">1</span>
                            </span>
                            <button class="btn btn-secondary" onclick="nextPage()" id="nextButton">
                                Next
                                <i class="fas fa-chevron-right"></i>
                            </button>
                        </div>
                    </section>
                </main>

                <!-- User Form Modal -->
                <div id="userModal" class="modal" style="display: none;">
                    <div class="modal-content" style="max-width: 600px;">
                        <div class="modal-header">
                            <h3 id="modalTitle">Add New User</h3>
                            <button class="modal-close" onclick="closeUserModal()">
                                <i class="fas fa-times"></i>
                            </button>
                        </div>
                        <form id="userForm" class="modal-body">
                            <input type="hidden" id="userId" name="id">

                            <div class="form-group">
                                <label for="userFullName" class="form-label">Full Name *</label>
                                <input type="text" id="userFullName" name="fullName" class="form-input" required>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="userUsername" class="form-label">Username *</label>
                                    <input type="text" id="userUsername" name="username" class="form-input" required>
                                </div>
                                <div class="form-group">
                                    <label for="userEmail" class="form-label">Email *</label>
                                    <input type="email" id="userEmail" name="email" class="form-input" required>
                                </div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                                <div class="form-group">
                                    <label for="userPhone" class="form-label">Phone</label>
                                    <input type="tel" id="userPhone" name="phone" class="form-input">
                                </div>
                                <div class="form-group">
                                    <label for="userRole" class="form-label">Role *</label>
                                    <select id="userRole" name="role" class="form-select" required>
                                        <option value="CUSTOMER">Customer</option>
                                        <option value="SALES">Sales</option>
                                        <option value="MANAGER">Manager</option>
                                        <option value="ADMIN">Admin</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="userAddress" class="form-label">Address</label>
                                <textarea id="userAddress" name="address" class="form-textarea" rows="3"></textarea>
                            </div>

                            <div class="form-group">
                                <label for="userProfileImage" class="form-label">Profile Image</label>
                                <div style="display: flex; gap: 0.5rem; align-items: center;">
                                    <input type="url" id="userProfileImage" name="profileImageUrl" class="form-input"
                                        placeholder="https://res.cloudinary.com/..." style="flex: 1;">
                                    <button type="button" class="btn btn-secondary" onclick="uploadProfileImage()">
                                        <i class="fas fa-cloud-upload-alt"></i>
                                        Upload
                                    </button>
                                </div>
                                <div id="profileImagePreview" class="image-preview"
                                    style="margin-top: 0.5rem; display: none;">
                                    <img id="profileImageImg" alt="Profile Preview"
                                        style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover;">
                                </div>
                            </div>

                            <div class="form-group" id="passwordSection">
                                <label for="userPassword" class="form-label">Password <span
                                        id="passwordRequired">*</span></label>
                                <input type="password" id="userPassword" name="password" class="form-input"
                                    placeholder="Leave empty to keep current password">
                            </div>

                            <div class="form-group">
                                <label class="checkbox-label">
                                    <input type="checkbox" id="userActive" name="active" checked>
                                    <span class="checkmark"></span>
                                    Active User
                                </label>
                            </div>
                        </form>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" onclick="closeUserModal()">Cancel</button>
                            <button type="button" class="btn btn-primary" onclick="saveUser()">Save User</button>
                        </div>
                    </div>
                </div>

                <!-- Toast Notification -->
                <div id="toast" class="toast"></div>

                <script>
                    var currentPageVar = <c:out value="${currentPage != null ? currentPage : 1}" />;
                    var totalUsersVar = <c:out value="${users != null ? users.size() : 0}" />;
                    var usersPerPage = 20;
                    var allUsers = [];

                    // Initialize
                    document.addEventListener('DOMContentLoaded', function () {
                        loadUsers();
                        setupEventListeners();
                    });

                    function setupEventListeners() {
                        // Search functionality
                        document.getElementById('searchUsers').addEventListener('input', function (e) {
                            filterUsers();
                        });

                        // Filter functionality
                        document.getElementById('roleFilter').addEventListener('change', filterUsers);
                        document.getElementById('statusFilter').addEventListener('change', filterUsers);
                    }

                    function loadUsers() {
                        fetch('${pageContext.request.contextPath}/admin/users/api?page=' + currentPageVar)
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    allUsers = data.users;
                                    renderUsers(allUsers);
                                    updatePagination(data.totalPages);
                                } else {
                                    showToast('Failed to load users: ' + data.message, 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error loading users:', error);
                                showToast('Failed to load users', 'error');
                            });
                    }

                    function renderUsers(users) {
                        // This function would typically re-render the table
                        // For now, we'll use the server-side rendered content
                    }

                    function filterUsers() {
                        const searchTerm = document.getElementById('searchUsers').value.toLowerCase();
                        const roleFilter = document.getElementById('roleFilter').value;
                        const statusFilter = document.getElementById('statusFilter').value;

                        const rows = document.querySelectorAll('#usersTable tbody tr');

                        rows.forEach(row => {
                            const userId = row.dataset.userId;
                            if (!userId) return; // Skip empty state row

                            const fullName = row.querySelector('td:first-child .user-avatar + div > div:first-child').textContent.toLowerCase();
                            const username = row.querySelector('td:first-child .user-avatar + div > div:last-child').textContent.toLowerCase();
                            const email = row.querySelector('td:nth-child(2) > div > div:first-child').textContent.toLowerCase();
                            const role = row.querySelector('.role-badge').textContent.trim();
                            const status = row.querySelector('.status-indicator').textContent.trim().toLowerCase();

                            const matchesSearch = !searchTerm || fullName.includes(searchTerm) ||
                                username.includes(searchTerm) || email.includes(searchTerm);
                            const matchesRole = !roleFilter || role === roleFilter;
                            const matchesStatus = !statusFilter || status.includes(statusFilter);

                            row.style.display = matchesSearch && matchesRole && matchesStatus ? '' : 'none';
                        });
                    }

                    function resetFilters() {
                        document.getElementById('searchUsers').value = '';
                        document.getElementById('roleFilter').value = '';
                        document.getElementById('statusFilter').value = '';
                        filterUsers();
                    }

                    function showUserForm(userId = null) {
                        document.getElementById('userModal').style.display = 'flex';
                        document.getElementById('userForm').reset();
                        document.getElementById('profileImagePreview').style.display = 'none';

                        if (userId) {
                            document.getElementById('modalTitle').textContent = 'Edit User';
                            document.getElementById('passwordRequired').textContent = '';
                            document.getElementById('userPassword').required = false;
                            document.getElementById('userPassword').placeholder = 'Leave empty to keep current password';
                            loadUserData(userId);
                        } else {
                            document.getElementById('modalTitle').textContent = 'Add New User';
                            document.getElementById('passwordRequired').textContent = '*';
                            document.getElementById('userPassword').required = true;
                            document.getElementById('userPassword').placeholder = 'Enter password';
                            document.getElementById('userId').value = '';
                        }
                    }

                    function loadUserData(userId) {
                        fetch(`${pageContext.request.contextPath}/admin/users/api/${userId}`)
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    const user = data.user;
                                    document.getElementById('userId').value = user.id;
                                    document.getElementById('userFullName').value = user.fullName || '';
                                    document.getElementById('userUsername').value = user.username || '';
                                    document.getElementById('userEmail').value = user.email || '';
                                    document.getElementById('userPhone').value = user.phone || '';
                                    document.getElementById('userRole').value = user.role || 'CUSTOMER';
                                    document.getElementById('userAddress').value = user.address || '';
                                    document.getElementById('userProfileImage').value = user.profileImageUrl || '';
                                    document.getElementById('userActive').checked = user.active;

                                    if (user.profileImageUrl) {
                                        updateProfileImagePreview(user.profileImageUrl);
                                    }
                                } else {
                                    showToast('Failed to load user data: ' + data.message, 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error loading user data:', error);
                                showToast('Failed to load user data', 'error');
                            });
                    }

                    function closeUserModal() {
                        document.getElementById('userModal').style.display = 'none';
                    }

                    function saveUser() {
                        const form = document.getElementById('userForm');
                        const formData = new FormData(form);

                        // Convert FormData to JSON
                        const userData = {};
                        formData.forEach((value, key) => {
                            if (key === 'active') {
                                userData[key] = document.getElementById('userActive').checked;
                            } else {
                                userData[key] = value;
                            }
                        });

                        const isEdit = userData.id && userData.id.trim() !== '';
                        const url = `${pageContext.request.contextPath}/admin/user/${isEdit ? 'update' : 'save'}`;

                        fetch(url, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify(userData)
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    showToast(data.message, 'success');
                                    closeUserModal();
                                    loadUsers(); // Reload the users list
                                } else {
                                    showToast('Failed to save user: ' + data.message, 'error');
                                }
                            })
                            .catch(error => {
                                console.error('Error saving user:', error);
                                showToast('Failed to save user', 'error');
                            });
                    }

                    function editUser(userId) {
                        showUserForm(userId);
                    }

                    function toggleUserStatus(userId, currentStatus) {
                        const action = currentStatus ? 'deactivate' : 'activate';
                        const message = currentStatus ? 'deactivate' : 'activate';

                        if (confirm(`Are you sure you want to ${message} this user?`)) {
                            fetch(`${pageContext.request.contextPath}/admin/user/toggle-status`, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: JSON.stringify({ id: userId, active: !currentStatus })
                            })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        showToast(data.message, 'success');
                                        loadUsers();
                                    } else {
                                        showToast('Failed to update user status: ' + data.message, 'error');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error updating user status:', error);
                                    showToast('Failed to update user status', 'error');
                                });
                        }
                    }

                    function deleteUser(userId) {
                        if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
                            fetch(`${pageContext.request.contextPath}/admin/user/delete`, {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: JSON.stringify({ id: userId })
                            })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        showToast(data.message, 'success');
                                        loadUsers();
                                    } else {
                                        showToast('Failed to delete user: ' + data.message, 'error');
                                    }
                                })
                                .catch(error => {
                                    console.error('Error deleting user:', error);
                                    showToast('Failed to delete user', 'error');
                                });
                        }
                    }

                    function uploadProfileImage() {
                        // Initialize Cloudinary user uploader
                        if (window.CloudinaryUploader) {
                            const userUploader = window.CloudinaryUploader.createUserUploader(
                                function (imageInfo) {
                                    console.log('Profile image uploaded:', imageInfo);
                                    const imageUrl = imageInfo.secure_url;
                                    document.getElementById('userProfileImage').value = imageUrl;
                                    updateProfileImagePreview(imageUrl);
                                    showToast('Profile image uploaded successfully!', 'success');
                                },
                                function (error) {
                                    console.error('Upload error:', error);
                                    showToast('Failed to upload image', 'error');
                                }
                            );
                            userUploader.open();
                        } else {
                            showToast('Upload service not available', 'error');
                        }
                    }

                    function updateProfileImagePreview(imageUrl) {
                        if (imageUrl) {
                            document.getElementById('profileImageImg').src = imageUrl;
                            document.getElementById('profileImagePreview').style.display = 'block';
                        } else {
                            document.getElementById('profileImagePreview').style.display = 'none';
                        }
                    }

                    function previousPage() {
                        if (currentPageVar > 1) {
                            currentPageVar--;
                            loadUsers();
                        }
                    }

                    function nextPage() {
                        currentPageVar++;
                        loadUsers();
                    }

                    function updatePagination(totalPages) {
                        document.getElementById('currentPage').textContent = currentPageVar;
                        document.getElementById('totalPages').textContent = totalPages;
                        document.getElementById('prevButton').disabled = currentPageVar <= 1;
                        document.getElementById('nextButton').disabled = currentPageVar >= totalPages;
                    }

                    function showToast(message, type = 'info') {
                        const toast = document.getElementById('toast');
                        toast.textContent = message;
                        toast.className = `toast toast-${type} show`;

                        setTimeout(() => {
                            toast.className = 'toast';
                        }, 3000);
                    }

                    // Profile image URL input listener
                    document.addEventListener('DOMContentLoaded', function () {
                        document.getElementById('userProfileImage').addEventListener('input', function (e) {
                            updateProfileImagePreview(e.target.value);
                        });
                    });
                </script>

                <style>
                    .role-badge {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.25rem;
                        padding: 0.25rem 0.5rem;
                        border-radius: 0.25rem;
                        font-size: 0.75rem;
                        font-weight: 600;
                        text-transform: uppercase;
                    }

                    .role-customer {
                        background: #e0f2fe;
                        color: #0277bd;
                    }

                    .role-sales {
                        background: #e8f5e8;
                        color: #2e7d32;
                    }

                    .role-manager {
                        background: #fff3e0;
                        color: #f57c00;
                    }

                    .role-admin {
                        background: #fce4ec;
                        color: #c2185b;
                    }

                    .auth-badge {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.25rem;
                        padding: 0.25rem 0.5rem;
                        border-radius: 0.25rem;
                        font-size: 0.75rem;
                        font-weight: 500;
                    }

                    .auth-local {
                        background: #f5f5f5;
                        color: #666;
                    }

                    .auth-google {
                        background: #fef7e0;
                        color: #f59e0b;
                    }

                    .status-indicator {
                        display: inline-flex;
                        align-items: center;
                        gap: 0.25rem;
                        font-size: 0.875rem;
                    }

                    .status-indicator.active {
                        color: #10b981;
                    }

                    .status-indicator.inactive {
                        color: #ef4444;
                    }

                    .status-indicator .fas.fa-circle {
                        font-size: 0.5rem;
                    }

                    .user-avatar img,
                    .user-avatar div {
                        border: 2px solid var(--border-color);
                    }

                    .image-preview {
                        text-align: center;
                    }

                    .checkbox-label {
                        display: flex;
                        align-items: center;
                        gap: 0.5rem;
                        cursor: pointer;
                    }

                    .checkbox-label input[type="checkbox"] {
                        width: auto;
                        margin: 0;
                    }

                    @media (max-width: 768px) {
                        .admin-filters>div {
                            grid-template-columns: 1fr;
                        }

                        .data-table {
                            font-size: 0.875rem;
                        }

                        .modal-content {
                            max-width: 95vw;
                            margin: 1rem;
                        }
                    }
                </style>
            </body>

            </html>