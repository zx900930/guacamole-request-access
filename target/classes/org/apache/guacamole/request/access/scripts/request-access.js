/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Request Access Extension - Main JavaScript Module
 */
var GuacRequestAccess = GuacRequestAccess || {};

/**
 * Request Access Controller
 */
GuacRequestAccess.Controller = function() {

    /**
     * Base URL for REST API endpoints
     */
    var API_BASE = '/api/session/ext/guac-request-access';

    /**
     * Translation strings
     */
    var translations = {};

    /**
     * Initialize the controller
     */
    var init = function() {
        loadTranslations();
        setupEventListeners();
        loadUserRequests();
    };

    /**
     * Load translation strings
     */
    var loadTranslations = function() {
        // Load translations from Guacamole's translation system
        if (window.GuacUI && window.GuacUI.Translations) {
            translations = window.GuacUI.Translations.REQUEST_ACCESS || {};
        }
    };

    /**
     * Setup event listeners
     */
    var setupEventListeners = function() {
        // Request form submission
        var requestForm = document.getElementById('request-access-form');
        if (requestForm) {
            requestForm.addEventListener('submit', handleRequestSubmit);
        }

        // Admin action buttons
        setupAdminActionListeners();
    };

    /**
     * Setup admin action listeners
     */
    var setupAdminActionListeners = function() {
        // Approve buttons
        var approveButtons = document.querySelectorAll('.approve-request');
        approveButtons.forEach(function(button) {
            button.addEventListener('click', function(e) {
                var requestId = e.target.dataset.requestId;
                approveRequest(requestId);
            });
        });

        // Reject buttons
        var rejectButtons = document.querySelectorAll('.reject-request');
        rejectButtons.forEach(function(button) {
            button.addEventListener('click', function(e) {
                var requestId = e.target.dataset.requestId;
                rejectRequest(requestId);
            });
        });

        // Cancel buttons
        var cancelButtons = document.querySelectorAll('.cancel-request');
        cancelButtons.forEach(function(button) {
            button.addEventListener('click', function(e) {
                var requestId = e.target.dataset.requestId;
                cancelRequest(requestId);
            });
        });
    };

    /**
     * Handle request form submission
     */
    var handleRequestSubmit = function(e) {
        e.preventDefault();
        
        var formData = {
            connectionId: document.getElementById('connection-id').value,
            connectionName: document.getElementById('connection-name').value,
            reason: document.getElementById('reason').value,
            startTime: new Date(document.getElementById('start-time').value).getTime(),
            endTime: new Date(document.getElementById('end-time').value).getTime()
        };

        submitRequest(formData);
    };

    /**
     * Submit a new access request
     */
    var submitRequest = function(requestData) {
        fetch(API_BASE + '/requests', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(function(response) {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Request submission failed');
        })
        .then(function(data) {
            showMessage(translations.MESSAGES?.REQUEST_SUBMITTED || 'Request submitted successfully', 'success');
            document.getElementById('request-access-form').reset();
            loadUserRequests();
        })
        .catch(function(error) {
            showMessage(translations.MESSAGES?.ERROR_SUBMITTING || 'Error submitting request', 'error');
            console.error('Error submitting request:', error);
        });
    };

    /**
     * Load user's requests
     */
    var loadUserRequests = function() {
        fetch(API_BASE + '/requests/my')
        .then(function(response) {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to load requests');
        })
        .then(function(requests) {
            displayRequests(requests, 'user-requests-list');
        })
        .catch(function(error) {
            console.error('Error loading requests:', error);
        });
    };

    /**
     * Load all requests (admin view)
     */
    var loadAllRequests = function() {
        fetch(API_BASE + '/requests')
        .then(function(response) {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to load requests');
        })
        .then(function(requests) {
            displayRequests(requests, 'admin-requests-list');
        })
        .catch(function(error) {
            console.error('Error loading requests:', error);
        });
    };

    /**
     * Display requests in a list
     */
    var displayRequests = function(requests, containerId) {
        var container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = '';

        if (requests.length === 0) {
            container.innerHTML = '<p>No requests found.</p>';
            return;
        }

        var table = document.createElement('table');
        table.className = 'request-access-list';
        
        // Create header
        var thead = document.createElement('thead');
        thead.innerHTML = `
            <tr>
                <th>${translations.REQUEST_LIST?.ID || 'ID'}</th>
                <th>${translations.REQUEST_LIST?.USER || 'User'}</th>
                <th>${translations.REQUEST_LIST?.CONNECTION || 'Connection'}</th>
                <th>${translations.REQUEST_LIST?.STATUS || 'Status'}</th>
                <th>${translations.REQUEST_LIST?.START_TIME || 'Start Time'}</th>
                <th>${translations.REQUEST_LIST?.END_TIME || 'End Time'}</th>
                <th>${translations.REQUEST_LIST?.ACTIONS || 'Actions'}</th>
            </tr>
        `;
        table.appendChild(thead);

        // Create body
        var tbody = document.createElement('tbody');
        requests.forEach(function(request) {
            var row = document.createElement('tr');
            row.className = 'request-access-item';
            
            var statusClass = 'status-' + request.status.toLowerCase();
            var statusText = translations.STATUS?.[request.status] || request.status;
            
            var actions = '';
            if (request.status === 'PENDING') {
                actions = `
                    <button class="approve-request" data-request-id="${request.requestId}">
                        ${translations.REQUEST_LIST?.APPROVE || 'Approve'}
                    </button>
                    <button class="reject-request" data-request-id="${request.requestId}">
                        ${translations.REQUEST_LIST?.REJECT || 'Reject'}
                    </button>
                `;
            } else if (request.status === 'APPROVED') {
                actions = `
                    <button class="reserve-request" data-request-id="${request.requestId}">
                        ${translations.REQUEST_LIST?.RESERVE || 'Reserve'}
                    </button>
                `;
            } else if (request.status === 'PENDING' || request.status === 'APPROVED') {
                actions = `
                    <button class="cancel-request" data-request-id="${request.requestId}">
                        ${translations.REQUEST_LIST?.CANCEL || 'Cancel'}
                    </button>
                `;
            }

            row.innerHTML = `
                <td>${request.requestId}</td>
                <td>${request.username}</td>
                <td>${request.connectionName}</td>
                <td><span class="request-access-status ${statusClass}">${statusText}</span></td>
                <td>${new Date(request.startTime).toLocaleString()}</td>
                <td>${new Date(request.endTime).toLocaleString()}</td>
                <td class="request-access-actions">${actions}</td>
            `;
            
            tbody.appendChild(row);
        });
        
        table.appendChild(tbody);
        container.appendChild(table);

        // Re-setup action listeners for new buttons
        setupAdminActionListeners();
    };

    /**
     * Approve a request
     */
    var approveRequest = function(requestId) {
        fetch(API_BASE + '/requests/' + requestId + '/approve', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function(response) {
            if (response.ok) {
                showMessage(translations.MESSAGES?.REQUEST_APPROVED || 'Request approved', 'success');
                loadAllRequests();
            } else {
                throw new Error('Failed to approve request');
            }
        })
        .catch(function(error) {
            showMessage(translations.MESSAGES?.ERROR_UPDATING || 'Error updating request', 'error');
            console.error('Error approving request:', error);
        });
    };

    /**
     * Reject a request
     */
    var rejectRequest = function(requestId) {
        var reason = prompt('Please provide a reason for rejection:');
        if (!reason) return;

        fetch(API_BASE + '/requests/' + requestId + '/reject', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ reason: reason })
        })
        .then(function(response) {
            if (response.ok) {
                showMessage(translations.MESSAGES?.REQUEST_REJECTED || 'Request rejected', 'success');
                loadAllRequests();
            } else {
                throw new Error('Failed to reject request');
            }
        })
        .catch(function(error) {
            showMessage(translations.MESSAGES?.ERROR_UPDATING || 'Error updating request', 'error');
            console.error('Error rejecting request:', error);
        });
    };

    /**
     * Cancel a request
     */
    var cancelRequest = function(requestId) {
        fetch(API_BASE + '/requests/' + requestId + '/cancel', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function(response) {
            if (response.ok) {
                showMessage(translations.MESSAGES?.REQUEST_CANCELLED || 'Request cancelled', 'success');
                loadUserRequests();
            } else {
                throw new Error('Failed to cancel request');
            }
        })
        .catch(function(error) {
            showMessage(translations.MESSAGES?.ERROR_UPDATING || 'Error updating request', 'error');
            console.error('Error cancelling request:', error);
        });
    };

    /**
     * Show message to user
     */
    var showMessage = function(message, type) {
        // Create message element
        var messageEl = document.createElement('div');
        messageEl.className = 'request-access-message message-' + type;
        messageEl.textContent = message;
        
        // Add to page
        var container = document.querySelector('.request-access-container') || document.body;
        container.insertBefore(messageEl, container.firstChild);
        
        // Auto-remove after 5 seconds
        setTimeout(function() {
            if (messageEl.parentNode) {
                messageEl.parentNode.removeChild(messageEl);
            }
        }, 5000);
    };

    /**
     * Load available connections
     */
    var loadAvailableConnections = function() {
        fetch(API_BASE + '/connections')
        .then(function(response) {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to load connections');
        })
        .then(function(connections) {
            var select = document.getElementById('connection-id');
            if (!select) return;
            
            select.innerHTML = '';
            connections.forEach(function(connection) {
                var option = document.createElement('option');
                option.value = connection.identifier;
                option.textContent = connection.name;
                select.appendChild(option);
            });
        })
        .catch(function(error) {
            console.error('Error loading connections:', error);
        });
    };

    // Public API
    return {
        init: init,
        loadUserRequests: loadUserRequests,
        loadAllRequests: loadAllRequests,
        loadAvailableConnections: loadAvailableConnections,
        submitRequest: submitRequest,
        approveRequest: approveRequest,
        rejectRequest: rejectRequest,
        cancelRequest: cancelRequest
    };

}();

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    GuacRequestAccess.Controller.init();
});