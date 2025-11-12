# microservice-admin (Java 21, Spring Boot 3.3.5)
Endpoints:
- GET  /admin/reportes/uso?desde=YYYY-MM-DD&hasta=YYYY-MM-DD&incluirPausas=true|false
- GET  /admin/reportes/monopatines/mas-viajes?anio=2025&minViajes=X
- GET  /admin/reportes/facturacion?anio=2025&mesDesde=1&mesHasta=6
- GET  /admin/reportes/usuarios?desde=YYYY-MM-DD&hasta=YYYY-MM-DD&tipo=PREMIUM
- GET  /admin/precios
- POST /admin/precios
- POST /admin/precios/ajustes
Actuator: /actuator/health
OpenAPI: /v3/api-docs (UI por gateway)
