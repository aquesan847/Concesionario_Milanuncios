<?php
    ini_set('display_errors', 1);
    error_reporting(E_ALL);
    require_once('settingsDB.php');
    require_once('DBConnection.php');
    $dbc = new DBConnection($dbsettings);
    
    //$url = 'https://www.milanuncios.com/volvo-de-segunda-mano/volvo-xc40-433186671.htm';
    $url = 'https://www.milanuncios.com/tienda/auto-suecia-49966.htm';
    
    $archivo = getUrls($url);
    getAd($archivo, $dbc);
    
    /*$file = "concesionario.txt";
    $fp = fopen($file, "w");
    fwrite($fp, $archivo);
    
    $csv = "coches.csv";
    $fp = fopen($csv, "w");
    $cabecera = "";
    fwrite($fp, $cabecera);
    */
    
    function getColor($archivo) {
        $str = '{\"type\":\"color\",\"value\":\"';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '\"},{\"type\":\"door';
        $final_pos = strpos($archivo, $str);
        
        $color = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $color;
        return $color;
    }
    
    
    function getDescription($archivo) {
        $str = 'description" content="';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '" data-reactroot=""/><link d';
        $final_pos = strpos($archivo, $str);
        
        $description = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        $description = str_replace('&quot;', "", $description);
        //echo $description;
        return $description;
    }
    
    
    function getFuel($archivo) {
        $str = 'type\":\"fuel\",\"value\":\"';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '\"},{\"type\":\"hp';
        $final_pos = strpos($archivo, $str);
        
        $fuel = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $fuel;
        return $fuel;
    }
    
    
    function getImgs($archivo) {
        $strNum = '/ 50';
        $lenghtNum = strlen($strNum);
        $init_pos = strpos($archivo, $strNum);
        
        $strNum = '/ 50';
        $lenghtNum2 = strlen($strNum);
        $final_pos = strpos($archivo, $strNum);
        
        $num = substr($archivo, $init_pos + 2, $final_pos - $init_pos + 4);
        $cont = 1;    
        $imgs = "";
        
        while ($cont <= $num) {
            $str = '"urlp\":\"';
            $lenght = strlen($str);
            $init_pos = strpos($archivo, $str);        
            
            
            $str = '.jpg';
            $lenght2 = strlen($str);
            $final_pos = strpos($archivo, $str);
            
            $img = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght - 1);
            $img = $img.$cont.".jpg\n";
            
            $imgs .= $img.";";
            //echo $img;
            $cont++;
            if ($cont >= 20){
                break;
            }
        }
        return $imgs;
    }
    
    
    function getKms($archivo) {
        $str = '{\"type\":\"kilometers\",\"value\":';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '},{\"type\":\"transmission';
        $final_pos = strpos($archivo, $str);
        
        $km = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $km." km"; 
        if (strlen($km) > 20) {
            $km = 0;
        }
        return $km;
    }
    
    
    function getLocal($archivo) {
        $str = 'class="ma-AdDetail-seoCategoryLink" title="';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '">#';
        $final_pos = strpos($archivo, $str);
        
        $local = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $local;
        return $local;
    }
    
    
    function getNumPuertas($archivo) {
        $str = '{\"type\":\"doors\",\"value\":';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '},{\"type\":\"fuel\",\"value';
        $final_pos = strpos($archivo, $str);
        
        
        
        $npuertas = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $npuertas." puertas";
        if (strpos($npuertas, 'environmental') !== false) {
            $str = '{\"type\":\"doors\",\"value\":';
            $lenght = strlen($str);
            $init_pos = strpos($archivo, $str);
            
            $str = '},{\"type\":\"environmentalLabel';
            $final_pos = strpos($archivo, $str);
            $npuertas = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        }
        return $npuertas;
    }
    
    function getPotencia($archivo) {
        $str = '{\"type\":\"hp\",\"value\":';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '},{\"type\":\"kilome';
        $final_pos = strpos($archivo, $str);
        
        $potencia = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        
        //echo $potencia."cv";
        if (strpos($potencia, 'transmission') !== false) {
            $str = '{\"type\":\"hp\",\"value\":';
            $lenght = strlen($str);
            $init_pos = strpos($archivo, $str);
            
            $str = '},{\"type\":\"transmission\",\"va';
            $final_pos = strpos($archivo, $str);
            $potencia = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        }
        return $potencia;
    }
    
    
    function getPrecio($archivo) {
        $str = '<span class="ma-AdPrice-value ma-AdPrice-value--default ma-AdPrice-value--heading--l">';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = "€";
        $final_pos = strpos($archivo, $str);
        
        $precio = substr($archivo, $init_pos, $final_pos - $init_pos - 2);
        $precio = str_replace(".", "", $precio);
        $precio = strip_tags($precio);
        //echo $precio;
        return $precio;
    } 
    
    
    function getRef($archivo) {
        $str = "Ref: ";
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = "Ref:  ";
        $lenght2 = 9;
        $final_pos = strpos($archivo, $str);
        
        $ref = substr($archivo, $init_pos + $lenght, $final_pos + $lenght2);
        //echo $ref;
        return $ref;
    }
    
    
    function getTitle($archivo) {
        $str = "Milanuncios - ";
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = "</title>";
        $final_pos = strpos($archivo, $str);
        
        $titulo = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $titulo;
        return $titulo;
    }
    
    
    function getTransmission($archivo) {
        $str = '{\"type\":\"transmission\",\"value\":\"';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '\"},{\"type\":\"warranty\"';
        $final_pos = strpos($archivo, $str);
        
        $cambio = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        
        //echo $cambio;
        if (strpos($cambio, 'year') !== false) {
            $str = '{\"type\":\"transmission\",\"value\":\"';
            $lenght = strlen($str);
            $init_pos = strpos($archivo, $str);
            
            $str = '\"},{\"type\":\"year';
            $final_pos = strpos($archivo, $str);
            $cambio = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        }
        return $cambio;
    }
    
    
    function getUrls($link) {
        $archivo = file_get_contents($link);
        if ($archivo == false) {
            echo "Error: url problem";
            exit(1);
        }
        return $archivo;
    }
    
    
    function getYear($archivo) {
        $str = '{\"type\":\"year\",\"value\":';
        $lenght = strlen($str);
        $init_pos = strpos($archivo, $str);
        
        $str = '}],\"legalAttributes\"';
        $final_pos = strpos($archivo, $str);
        
        $year = substr($archivo, $init_pos + $lenght, $final_pos - $init_pos - $lenght);
        //echo $year; 
        return $year;
    }
    
    function insertCoches($archivo, $link, $dbc) {
        $ref = getRef($archivo);
        $titulo = getTitle($archivo);
        $description = getDescription($archivo);
        $url = $link;
        $imgs = getImgs($archivo);
        $precio = getPrecio($archivo);
        $local = getLocal($archivo);
        $fuel = getFuel($archivo);
        $km = getKms($archivo);
        $cambio = getTransmission($archivo);
        $potencia = getPotencia($archivo);
        $npuertas = getNumPuertas($archivo);
        $color = getColor($archivo);
        $year = getYear($archivo);
        $sql = "INSERT INTO `coches` VALUES (".$ref.",'".$titulo."','".$description."','".$url."','".$imgs."',".$precio.",'".$local."','"
                .$fuel."',".$km.",'".$cambio."',".$potencia.",".$npuertas.",'".$color."',".$year.");";
        echo $sql;
        //exit;
            
                
        $afectedRows = $dbc->runQuery($sql);
        
        if ($afectedRows == 1) {
                    $error = 0;
                    $errormsg = '<br>Coches <strong>registrados</strong>...<br><br>';
                    echo $errormsg;
        } else {
                    // Message error
                    $error = 1;
                    $errormsg = '<br><br><strong>Sorry</strong>, something went <strong>wrong</strong> with the DDBB. Try again?<br><br>';
                    echo $errormsg;
        }
        
        //exit;
    }

    function getAd($archivo, $dbc) {
        $findme   = '"@type": "Product",';
        $extra = strlen($findme);
        $posInicio = strpos($archivo, $findme) + $extra;
        
        $numAd = substr_count($archivo,$findme);
    
        for ($i = 0; $i < $numAd; $i++) {
            
            $findme   = '"@type": "Product",';
            $extra = strlen($findme);
            $posInicio = strpos($archivo, $findme) + $extra;
            
            $findme   = '"name":';
            $posFinal = strpos($archivo, $findme);
            
            $urlAd = substr($archivo, $posInicio, $posFinal-$posInicio);
            $urlAd = str_replace('"url": "','',$urlAd);
            $urlAd = str_replace('",','',$urlAd);
            $urlAd = trim($urlAd);
            //writeAd(trim($urlAd),$dbc);
            $archivo2 = getUrls($urlAd);
            insertCoches($archivo2, $urlAd, $dbc);
            
            $findme   = '"description"';
            $posFinal = strpos($archivo, $findme);
            $archivo = substr($archivo, $posFinal+1);
            
        }
    }
    


?>
