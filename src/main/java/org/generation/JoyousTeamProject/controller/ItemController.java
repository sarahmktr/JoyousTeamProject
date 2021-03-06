package org.generation.JoyousTeamProject.controller;

import org.generation.JoyousTeamProject.component.FileUploadUtil;
import org.generation.JoyousTeamProject.controller.dto.ItemDTO;
import org.generation.JoyousTeamProject.repository.entity.Productlist;
import org.generation.JoyousTeamProject.service.*;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/productlist")    // a naming "/item" is given

public class ItemController {

    final ItemService itemService;

    @Value("${image.folder}")
    private String imageFolder;  // the path to the images folder

    //constructor
    public ItemController(@Autowired ItemService itemService) {
        this.itemService = itemService;
    }

    @CrossOrigin
    //Cross-origin resource sharing (CORS) provides security to prohibit AJAX calls to the resources residing outside
    // the current origin.
    @GetMapping("/all") // a naming "/all" is given
    //GetMapping is the route that correspond to the HTTP GET method calls from the client.

    public Iterable<Productlist> getItems() {
        return itemService.all();   //calling a method all() in the service package - ItemService interface
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Productlist findItemById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @CrossOrigin
    @DeleteMapping("/{id}") //  {id} entry of the id value (integer)
    public void delete(@PathVariable Integer id) {
        itemService.delete(id);
    }

    @CrossOrigin
    @PostMapping("/add")
    //@RequestParam method to pass the information received from the clients
    public Productlist save(@RequestParam(name = "name", required = true) String name,
                     @RequestParam(name = "description", required = true) String description,
                     @RequestParam(name = "imageURL", required = true) String imageURL,
                     //imageUrl is the image name, not the actual image file/object
                     //imageUrl = images/T-shirt5.png (it is a string)
                     @RequestParam(name = "category", required = true) String category,
                     @RequestParam(name = "price", required = true) double price,
                     @RequestParam("imagefile") MultipartFile multipartFile) throws IOException
                    //image conversion done here
    {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        FileUploadUtil.saveFile(imageFolder, fileName, multipartFile);

        ItemDTO itemDto = new ItemDTO(name, description, imageURL, category, price);
        return itemService.save(new Productlist(itemDto));
    }


}


