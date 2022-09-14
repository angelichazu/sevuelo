package ec.sevolutivo.sevuelos.sevuelos.web.rest;

import ec.sevolutivo.sevuelos.sevuelos.domain.Request;
import ec.sevolutivo.sevuelos.sevuelos.domain.enumeration.RequestStatus;
import ec.sevolutivo.sevuelos.sevuelos.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://localhost:8080"})
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    private final RequestRepository requestRepository;

    public RequestResource(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @PostMapping("/requests")
    public Request createRequest(@RequestBody Request request) {
        log.debug("REST request to save Request : {}", request);
        if (request.getId() != null) {
            throw new RuntimeException("A new request cannot already have an ID");
        }
        request.setStatus(RequestStatus.NEW);
        return requestRepository.save(request);
    }

    @GetMapping("/requests")
    public List<Request> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestRepository.findAll();
    }

    @GetMapping("/requests/{id}")
    public Request getRequest(@PathVariable Long id) {
        log.debug("REST request to get Request : {}", id);
        Optional<Request> request = requestRepository.findById(id);
        return request.get();
    }

    //Se agregó un nuevo WS para hacer uso de la consulta nueva y adicionalmente se utilizó una nueva estructura de consumo de manera demostrativa.
    @GetMapping("/requestsByDestination")
    public List<Request> getRequestByDestination(@RequestParam(name = "destination", required = true) String destination) {
        log.debug("REST request to get Request by destination: {}", destination);
        List<Request> requestList = requestRepository.findAllByDestination(destination);
        return requestList;
    }

    //Fue modificado el servicio, ya que era muy susceptible a errores al no tener un control del ID
    @PutMapping("/reserve/{id}")
    public void reserve(@PathVariable Long id) {
        log.debug("REST request to reserve a flight");
        try {
            Optional<Request> requestFind = requestRepository.findById(id);
            if (!requestFind.isPresent()) {
                throw new RuntimeException("The requested flight is not available");
            }
            requestFind.get().setStatus(RequestStatus.RESERVED);
            requestRepository.save(requestFind.get());
        } catch (Exception e) {
            log.debug("Error in REST request to reserve a flight: {}", e.getMessage());
            throw new RuntimeException("A problem has occurred, please contact the administrator.");
        }
    }

}
