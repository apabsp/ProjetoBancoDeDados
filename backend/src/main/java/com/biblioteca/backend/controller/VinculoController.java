import com.biblioteca.backend.dto.VinculaGeneroObraDTO;
import com.biblioteca.backend.dto.VinculaObraEditoraDTO;
import com.biblioteca.backend.dto.VinculoAutorObraDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vinculo")
public class VinculoController {
    @Autowired
    private DatabaseService db;

    @PostMapping("/obra-editora")
    public String vincularObraAEditora(@RequestBody VinculaObraEditoraDTO dto) {
        return db.vincularObraAEditora(dto.getCodBarras(), dto.getIdEditora());
    }

    @PostMapping("/autor-obra")
    public String vincularAutorAObra(@RequestBody VinculoAutorObraDTO dto) {
        return db.vincularAutorAObra(dto.getIdAutor(), dto.getCodBarras());
    }

    @PostMapping("/genero-obra")
    public String vincularGeneroAObra(@RequestBody VinculaGeneroObraDTO dto) {
        return db.vincularGeneroAObra(dto.getNomeGenero(), dto.getCodBarras());
    }
}
