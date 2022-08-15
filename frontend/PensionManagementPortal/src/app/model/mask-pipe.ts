import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'mask' })
export class MaskPipe implements PipeTransform {
    transform(phrase: String) {    
        let toBeReplaced = phrase.slice(0, 6);
        return phrase.replace(toBeReplaced, "xxxxxx");
    }
}