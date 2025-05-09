<%@ include file="/header.jsp" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
<div class="container mt-4">
    <h2 class="mb-4">Technician Dashboard</h2>

    <div class="row g-4">
        <div class="col-md-4">
            <a href="requests.jsp" class="text-decoration-none">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <h5 class="card-title">Service Requests</h5>
                        <p class="card-text">View and manage employee service requests.</p>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="equipment.jsp?filter=repair" class="text-decoration-none">
                <div class="card text-white bg-warning">
                    <div class="card-body">
                        <h5 class="card-title">Repair Equipment</h5>
                        <p class="card-text">View and repair NOT_WORKING or DAMAGED equipment.</p>
                    </div>
                </div>
            </a>
        </div>

        <div class="col-md-4">
            <a href="equipment.jsp?filter=replace" class="text-decoration-none">
                <div class="card text-white bg-danger">
                    <div class="card-body">
                        <h5 class="card-title">Replace Old Equipment</h5>
                        <p class="card-text">View and replace OLD equipment.</p>
                    </div>
                </div>
            </a>
        </div>
    </div>
</div>