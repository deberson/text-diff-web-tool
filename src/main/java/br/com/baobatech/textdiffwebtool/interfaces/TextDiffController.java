package br.com.baobatech.textdiffwebtool.interfaces;

import br.com.baobatech.textdiffwebtool.domain.TextComparator;
import br.com.baobatech.textdiffwebtool.domain.model.Side;
import br.com.baobatech.textdiffwebtool.domain.model.TextDiff;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(path = "/api")
public class TextDiffController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TextDiffController.class);

  private final TextComparator comparator;

  @Autowired
  public TextDiffController(
      final TextComparator comparator
  ) {
    this.comparator = comparator;
  }

  @ApiOperation(value = "Adds a value")
  @PostMapping(path = "/v1/diff/{id}/{side}")
  public ResponseEntity<Void> addValue(
      @PathVariable("id") final String key,
      @PathVariable("side") final Side side,
      @RequestBody final TextValueRequest text
  ) {
    LOGGER.trace("entry");
    try {
      comparator.addValue(key, side, text.value);
      return new ResponseEntity<>(HttpStatus.CREATED);

    } finally {
      LOGGER.trace("exit");
    }
  }

  @ApiOperation(value = "Computes the text diff from values previously sent")
  @GetMapping(path = "/v1/diff/{id}")
  public ResponseEntity<TextDiff> getDiff(
      @PathVariable("id") final String key
  ) {
    LOGGER.trace("entry");
    try {
      return new ResponseEntity<>(comparator.compare(key), HttpStatus.OK);

    } finally {
      LOGGER.trace("exit");
    }
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Side.class, new CaseInsensitiveConverter<>(Side.class));
  }
}
