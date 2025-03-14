# Load Balancer

This project implements a simple load balancer in Java that distributes incoming requests amongst a pool of backend servers while monitoring their health. It features:

- Round-robin load balancing: Requests are evenly distributed across available servers.
- Active-passive server management: Only healthy servers receive requests. Unhealthy servers are automatically disabled until they recover.
- Periodic health checks: Each server undergoes regular health checks to assess its status.